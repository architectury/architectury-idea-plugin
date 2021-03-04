package me.shedaniel.architectury.idea.inspection

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project

class CompoundFix(
    private val familyName: String,
    private val fixes: Collection<LocalQuickFix>
) : LocalQuickFix {
    override fun getFamilyName() = familyName

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        for (fix in fixes) {
            fix.applyFix(project, descriptor)
        }
    }
}
