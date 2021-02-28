package me.shedaniel.architectury.idea

import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope

const val EXPECT_PLATFORM = "me.shedaniel.architectury.annotations.ExpectPlatform"
val PLATFORMS = setOf("fabric", "forge")

val PsiMethod.isStatic: Boolean
    get() = modifierList.hasModifierProperty(PsiModifier.STATIC)

val PsiMethod.hasExpectPlatform: Boolean
    get() = isStatic && hasAnnotation(EXPECT_PLATFORM)

// TODO: Cache these somehow? Both commonMethods and platformMethods might be really slow and could benefit from caching.

/**
 * The common declarations corresponding to this platform method.
 */
val PsiMethod.commonMethods: List<PsiMethod>
    get() {
        // no common methods for non-statics
        if (!isStatic) return emptyList()

        val clazz = containingClass ?: return emptyList()
        val name = clazz.binaryName ?: return emptyList()
        val pkg = name.substringBeforeLast('.')

        val nameMatches = name.endsWith("Impl") && PLATFORMS.any { pkg.endsWith(".$it") }
        if (!nameMatches) return emptyList()

        val commonPkg = pkg.substringBeforeLast('.')
        val commonClassName = name.substringAfterLast('.').removeSuffix("Impl")
        val baseClass = "$commonPkg.$commonClassName"

        return JavaPsiFacade.getInstance(project).findPackage(commonPkg)
            ?.classes
            ?.asSequence()
            ?.flatMap {
                sequence {
                    yield(it)
                    yieldAll(it.innerClasses.asSequence())
                }
            }
            ?.filter { it.binaryName?.replace("$", "") == baseClass }
            ?.mapNotNull {
                it.findMethodBySignature(this, false)
            }
            ?.filter { it.hasExpectPlatform }
            ?.toList()
            ?: emptyList()
    }

/**
 * The platform implementations of this common method.
 */
val PsiMethod.platformMethods: List<PsiMethod>
    get() {
        if (!hasExpectPlatform) return emptyList()

        val containingClassName = containingClass?.binaryName ?: return emptyList()
        val parts = containingClassName.split('.')
        val head = parts.dropLast(1).joinToString(separator = ".")
        val tail = parts.last().replace("$", "")

        return PLATFORMS.asSequence().flatMap { platform ->
            val implementationClassName = "$head.$platform.${tail}Impl"

            JavaPsiFacade.getInstance(project)
                .findClasses(implementationClassName, GlobalSearchScope.projectScope(project))
                .asSequence()
                .mapNotNull { clazz ->
                    clazz.findMethodBySignature(this, false)
                }
        }.toList()
    }

/**
 * The binary name of this class in dot-dollar format (eg. `a.b.C$D`)
 */
val PsiClass.binaryName: String?
    get() =
        if (containingClass != null) containingClass!!.binaryName + "$" + name
        else qualifiedName
