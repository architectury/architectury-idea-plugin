package me.shedaniel.architectury.idea.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.findExpectPlatform
import me.shedaniel.architectury.idea.util.isCommonExpectPlatform
import me.shedaniel.architectury.idea.util.platformMethods

class UnimplementedExpectPlatformInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethod(method: PsiMethod) {
                if (method.isCommonExpectPlatform && method.platformMethods.isEmpty()) {
                    holder.registerProblem(
                        method.findExpectPlatform() ?: method,
                        ArchitecturyBundle["inspection.missingExpectPlatform", method.name],
                        ImplementExpectPlatformFix
                    )
                }
            }
        }
}
