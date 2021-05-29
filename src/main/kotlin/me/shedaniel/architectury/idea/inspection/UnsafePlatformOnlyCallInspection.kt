package me.shedaniel.architectury.idea.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiMethodCallExpression
import com.intellij.psi.util.PsiTreeUtil
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.isCommon
import me.shedaniel.architectury.idea.util.platformOnlyPlatforms

class UnsafePlatformOnlyCallInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethodCallExpression(expression: PsiMethodCallExpression) {
                val module = ModuleUtil.findModuleForPsiElement(expression)
                if (module != null && !module.isCommon) return

                val calledMethod = expression.resolveMethod()
                if (calledMethod == null) {
                    LOGGER.warn("Could not resolve method for $calledMethod")
                    return
                }

                val calledModule = ModuleUtil.findModuleForPsiElement(calledMethod)
                if (calledModule != null && !calledModule.isCommon) return

                val calledPlatforms = calledMethod.platformOnlyPlatforms ?: return
                val method = PsiTreeUtil.getParentOfType(expression, PsiMethod::class.java)
                if (method == null) {
                    LOGGER.warn("Could not find parent method for $expression")
                    return
                }

                val ownPlatforms = method.platformOnlyPlatforms
                val incorrectPlatform = ownPlatforms == null || ownPlatforms.any { it !in calledPlatforms }

                if (incorrectPlatform) {
                    holder.registerProblem(
                        expression.methodExpression,
                        ArchitecturyBundle["inspection.unsafePlatformOnlyCall", calledMethod.name]
                    )
                }
            }
        }

    companion object {
        private val LOGGER = Logger.getInstance(UnsafePlatformOnlyCallInspection::class.java)
    }
}
