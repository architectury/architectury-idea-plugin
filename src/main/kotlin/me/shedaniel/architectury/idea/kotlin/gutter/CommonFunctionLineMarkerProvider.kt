package me.shedaniel.architectury.idea.kotlin.gutter

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.kotlin.util.platformMethods
import org.jetbrains.kotlin.psi.KtNamedFunction
import javax.swing.Icon

class CommonFunctionLineMarkerProvider : KotlinRelatedMethodLineMarkerProvider() {
    override val tooltipTranslationKey = "architectury.gutter.goToPlatform"
    override val navTitleTranslationKey = "architectury.gutter.chooseImpl"
    override val KtNamedFunction.relatedMethods: Set<PsiMethod> get() = platformMethods

    override fun getName(): String = "ExpectPlatform line marker (Kotlin)"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementedMethod
}
