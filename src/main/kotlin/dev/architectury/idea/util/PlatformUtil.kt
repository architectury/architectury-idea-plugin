package dev.architectury.idea.util

import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.PsiModifierListOwner
import com.intellij.psi.search.GlobalSearchScope

val PsiMethod.isStatic: Boolean
    get() = modifierList.hasModifierProperty(PsiModifier.STATIC)

fun PsiModifierListOwner.hasAnnotation(type: AnnotationType): Boolean =
    type.any { hasAnnotation(it) }

/**
 * True if this method is a common, untransformed `@ExpectPlatform` method.
 */
val PsiMethod.isCommonExpectPlatform: Boolean
    get() = isStatic &&
        hasAnnotation(AnnotationType.EXPECT_PLATFORM) &&
        !hasAnnotation(AnnotationType.TRANSFORMED_EXPECT_PLATFORM)

/**
 * Finds the first annotation of the [type] on this method.
 */
fun PsiMethod.findAnnotation(type: AnnotationType): PsiAnnotation? =
    annotations.firstOrNull {
        type.any { name -> it.hasQualifiedName(name) }
    }

// TODO: Cache these somehow? Both commonMethods and platformMethods might be really slow and could benefit from caching.

/**
 * The common declarations corresponding to this platform method.
 */
val PsiMethod.commonMethods: Set<PsiMethod>
    get() {
        // no common methods for non-statics
        if (!isStatic) return emptySet()

        val clazz = containingClass ?: return emptySet()
        val name = clazz.binaryName ?: return emptySet()
        val pkg = name.substringBeforeLast('.')

        val nameMatches = name.endsWith("Impl") && Platform.values().any { pkg.endsWith(".${it.id}") }
        if (!nameMatches) return emptySet()

        val commonPkg = pkg.substringBeforeLast('.')
        val commonClassName = name.substringAfterLast('.').removeSuffix("Impl")
        val baseClass = "$commonPkg.$commonClassName"

        return JavaPsiFacade.getInstance(project).findPackage(commonPkg)
            ?.getClasses(getScopeFor(this))
            ?.asSequence()
            ?.flatMap { it.asSequenceWithInnerClasses() }
            ?.filter { it.binaryName?.replace("$", "") == baseClass }
            ?.mapNotNull {
                it.findMethodBySignature(this, false)
            }
            ?.filter { it.isCommonExpectPlatform }
            ?.toSet()
            ?: emptySet()
    }

/**
 * The platform implementations of this common method.
 */
val PsiMethod.platformMethodsByPlatform: Map<Platform, Set<PsiMethod>>
    get() {
        if (!isCommonExpectPlatform) return emptyMap()
        val clazz = containingClass ?: return emptyMap()

        return Platform.values().associateWith {
            val implementationClassName = it.getImplementationName(clazz)

            JavaPsiFacade.getInstance(project)
                .findClasses(implementationClassName, getScopeFor(this))
                .asSequence()
                .mapNotNull { clazz ->
                    clazz.findMethodBySignature(this, false)
                }
                .toSet()
        }
    }

/**
 * The platform implementations of this common method.
 */
val PsiMethod.platformMethods: Set<PsiMethod>
    get() = platformMethodsByPlatform.flatMap { (_, methods) -> methods }.toSet()

/**
 * The platforms for this `@PlatformOnly` method, or null if this method
 * is not platform-specific.
 */
val PsiMethod.platformOnlyPlatforms: Set<String>?
    get() {
        val annotation = findAnnotation(AnnotationType.PLATFORM_ONLY) ?: return null
        return Annotations.getStrings(annotation, "value").toSet()
    }

/**
 * The binary name of this class in dot-dollar format (eg. `a.b.C$D`)
 */
val PsiClass.binaryName: String?
    get() =
        if (containingClass != null) containingClass!!.binaryName + "$" + name
        else qualifiedName

/**
 * Gets a sequence of this class and all its inner classes, recursed infinitely.
 */
fun PsiClass.asSequenceWithInnerClasses(): Sequence<PsiClass> =
    sequence {
        yield(this@asSequenceWithInnerClasses)
        yieldAll(innerClasses.asSequence().flatMap { it.asSequenceWithInnerClasses() })
    }

/**
 * Gets a value from this platform map, falling back to the [Platform.fallbackPlatforms] if not specified here.
 */
fun <V : Any> Map<Platform, V>.getWithPlatformFallback(platform: Platform): V? =
    this[platform] ?: platform.fallbackPlatforms.asSequence().mapNotNull { getWithPlatformFallback(it) }.firstOrNull()

/**
 * Gets the searching scope for searching for classes related to the [element].
 * If the element's corresponding module is not null (= an element in this project),
 * uses the project scope. Otherwise uses the all scope.
 */
private fun getScopeFor(element: PsiElement): GlobalSearchScope =
    if (ModuleUtil.findModuleForPsiElement(element) != null) GlobalSearchScope.projectScope(element.project)
    else GlobalSearchScope.allScope(element.project)
