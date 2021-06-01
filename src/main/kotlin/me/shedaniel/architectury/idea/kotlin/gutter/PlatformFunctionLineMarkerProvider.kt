package me.shedaniel.architectury.idea.kotlin.gutter

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.kotlin.util.commonMethods
import org.jetbrains.kotlin.psi.KtNamedFunction
import javax.swing.Icon

class PlatformFunctionLineMarkerProvider : KotlinRelatedMethodLineMarkerProvider() {
    override val tooltipTranslationKey = "architectury.gutter.goToCommon"
    override val navTitleTranslationKey = "architectury.gutter.chooseCommon"
    override val KtNamedFunction.relatedMethods: Set<PsiMethod> get() = commonMethods

    override fun getName(): String = "Platform implementation method line marker (Kotlin)"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementingMethod
}
