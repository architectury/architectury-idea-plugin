package me.shedaniel.architectury.idea.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.Platform
import me.shedaniel.architectury.idea.util.findExpectPlatform
import me.shedaniel.architectury.idea.util.getWithPlatformFallback
import me.shedaniel.architectury.idea.util.isCommonExpectPlatform
import me.shedaniel.architectury.idea.util.platformMethodsByPlatform

class UnimplementedExpectPlatformInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethod(method: PsiMethod) {
                if (method.isCommonExpectPlatform) {
                    val allPlatformMethods = method.platformMethodsByPlatform
                    val missingPlatforms = Platform.values().filter { platform ->
                        if (!platform.isIn(method.project)) return@filter false

                        val methods = allPlatformMethods.getWithPlatformFallback(platform)
                        methods == null || methods.isEmpty()
                    }

                    if (missingPlatforms.isNotEmpty()) {
                        val fixes = missingPlatforms.mapTo(ArrayList()) { ImplementExpectPlatformFix(listOf(it)) }
                        if (fixes.size > 1) {
                            fixes.add(0, ImplementExpectPlatformFix(missingPlatforms))
                        }

                        holder.registerProblem(
                            method.findExpectPlatform() ?: method,
                            ArchitecturyBundle["inspection.missingExpectPlatform", method.name, missingPlatforms.joinToString()],
                            *fixes.toTypedArray()
                        )
                    }
                }
            }
        }
}
