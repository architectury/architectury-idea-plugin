package me.shedaniel.architectury.idea.inspection

import com.intellij.codeInsight.generation.GenerateMembersUtil
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.components.JBLabel
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.EXPECT_PLATFORM
import me.shedaniel.architectury.idea.util.OLD_EXPECT_PLATFORM
import me.shedaniel.architectury.idea.util.Platform

class ImplementExpectPlatformFix(private val platform: Platform) : LocalQuickFix {
    override fun getFamilyName(): String = ArchitecturyBundle["inspection.implementExpectPlatform.single", platform]

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val method = findMethod(descriptor.psiElement) ?: error("Could not find method from ${descriptor.psiElement}")
        val facade = JavaPsiFacade.getInstance(project)
        val elementFactory = JavaPsiFacade.getElementFactory(project)

        val implClassName = platform.getImplementationName(method.containingClass!!)
        val implClass = facade.findClass(implClassName, GlobalSearchScope.projectScope(project)) ?: run {
            val packageName = implClassName.substringBeforeLast('.')
            val pkg = facade.findPackage(packageName)

            if (pkg == null) {
                val label = JBLabel(ArchitecturyBundle["fix.missingPlatformPackage", packageName])

                return JBPopupFactory.getInstance()
                    .createComponentPopupBuilder(label, null)
                    .createPopup()
                    .showInBestPositionFor(FileEditorManager.getInstance(project).selectedTextEditor!!)
            }

            val dir = pkg.directories.first()
            JavaDirectoryService.getInstance().createClass(dir, implClassName.substringAfterLast('.'))
        }

        val template = elementFactory.createMethod(method.name, method.returnType)

        // Add the different modifiers. The public modifier is first removed to correct its place.
        template.modifierList.setModifierProperty(PsiModifier.PUBLIC, false) // remove from list...
        GenerateMembersUtil.copyAnnotations(method, template, EXPECT_PLATFORM, OLD_EXPECT_PLATFORM)
        template.modifierList.setModifierProperty(PsiModifier.PUBLIC, true) // ... add to list
        template.modifierList.setModifierProperty(PsiModifier.STATIC, true)

        (GenerateMembersUtil.insert(implClass, template, implClass.methods.lastOrNull(), false) as? PsiMethod)
            ?.navigate(true)
    }

    private tailrec fun findMethod(element: PsiElement): PsiMethod? =
        when {
            element is PsiMethod -> element
            element.parent != null -> findMethod(element.parent)
            else -> null
        }
}
