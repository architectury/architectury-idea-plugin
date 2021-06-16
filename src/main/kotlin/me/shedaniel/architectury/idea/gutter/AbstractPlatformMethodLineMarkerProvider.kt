package me.shedaniel.architectury.idea.gutter

import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.util.commonMethods
import javax.swing.Icon

abstract class AbstractPlatformMethodLineMarkerProvider<M : PsiElement> :
    RelatedMethodLineMarkerProvider<M>() {
    override val tooltipTranslationKey = "architectury.gutter.goToCommon"
    override val navTitleTranslationKey = "architectury.gutter.chooseCommon"
    override val PsiMethod.relatedMethods: Set<PsiMethod> get() = commonMethods

    override fun getName(): String = "Platform implementation method line marker"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementingMethod
}
