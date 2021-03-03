package me.shedaniel.architectury.idea.inspection

import com.intellij.codeInsight.generation.GenerateMembersUtil
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.EXPECT_PLATFORM
import me.shedaniel.architectury.idea.util.OLD_EXPECT_PLATFORM
import me.shedaniel.architectury.idea.util.PLATFORMS
import me.shedaniel.architectury.idea.util.getPlatformImplementationName

object ImplementExpectPlatformFix : LocalQuickFix {
    override fun getFamilyName(): String = ArchitecturyBundle["inspection.implementExpectPlatform"]

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val method = findMethod(descriptor.psiElement) ?: error("Could not find method from ${descriptor.psiElement}")
        val facade = JavaPsiFacade.getInstance(project)
        val elementFactory = JavaPsiFacade.getElementFactory(project)

        val generatedMethods = PLATFORMS.mapNotNull method@{ platform ->
            // TODO: Generate impl classes
            val implClass = facade.findClass(
                method.containingClass?.getPlatformImplementationName(platform) ?: return@method null,
                GlobalSearchScope.projectScope(project)
            ) ?: return@method null

            val template = elementFactory.createMethod(method.name, method.returnType)

            // Add the different modifiers. The public modifier is first removed to correct its place.
            template.modifierList.setModifierProperty(PsiModifier.PUBLIC, false) // remove from list...
            GenerateMembersUtil.copyAnnotations(method, template, EXPECT_PLATFORM, OLD_EXPECT_PLATFORM)
            template.modifierList.setModifierProperty(PsiModifier.PUBLIC, true) // ... add to list
            template.modifierList.setModifierProperty(PsiModifier.STATIC, true)

            GenerateMembersUtil.insert(implClass, template, implClass.methods.lastOrNull(), false) as PsiMethod
        }

        generatedMethods.firstOrNull()?.navigate(true)
    }

    private tailrec fun findMethod(element: PsiElement): PsiMethod? =
        when {
            element is PsiMethod -> element
            element.parent != null -> findMethod(element.parent)
            else -> null
        }
}
