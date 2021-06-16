package me.shedaniel.architectury.idea.gutter

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameIdentifierOwner
import me.shedaniel.architectury.idea.util.platformMethods
import javax.swing.Icon

abstract class AbstractCommonMethodLineMarkerProvider<M : PsiNameIdentifierOwner> :
    RelatedMethodLineMarkerProvider<M>() {
    override val tooltipTranslationKey = "architectury.gutter.goToPlatform"
    override val navTitleTranslationKey = "architectury.gutter.chooseImpl"
    override val PsiMethod.relatedMethods: Set<PsiMethod> get() = platformMethods

    override fun getName(): String = "ExpectPlatform line marker"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementedMethod
}
