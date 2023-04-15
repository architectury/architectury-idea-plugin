package dev.architectury.idea.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.JavaElementVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiMethod
import dev.architectury.idea.util.AnnotationType
import dev.architectury.idea.util.ArchitecturyBundle
import dev.architectury.idea.util.Platform
import dev.architectury.idea.util.findAnnotation
import dev.architectury.idea.util.getWithPlatformFallback
import dev.architectury.idea.util.isCommonExpectPlatform
import dev.architectury.idea.util.platformMethodsByPlatform

class UnimplementedExpectPlatformInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : JavaElementVisitor() {
            override fun visitMethod(method: PsiMethod) {
                if (method.isCommonExpectPlatform) {
                    val allPlatformMethods = method.platformMethodsByPlatform
                    val missingPlatforms = Platform.values().filter { platform ->
                        if (!platform.isIn(method.project)) return@filter false

                        val methods = allPlatformMethods.getWithPlatformFallback(platform)
                        methods.isNullOrEmpty()
                    }

                    if (missingPlatforms.isNotEmpty()) {
                        val fixes = missingPlatforms.mapTo(ArrayList()) { ImplementExpectPlatformFix(listOf(it)) }
                        if (fixes.size > 1) {
                            fixes.add(0, ImplementExpectPlatformFix(missingPlatforms))
                        }

                        holder.registerProblem(
                            method.findAnnotation(AnnotationType.EXPECT_PLATFORM) ?: method,
                            ArchitecturyBundle["inspection.missingExpectPlatform", method.name, missingPlatforms.joinToString()],
                            *fixes.toTypedArray()
                        )
                    }
                }
            }
        }
}
