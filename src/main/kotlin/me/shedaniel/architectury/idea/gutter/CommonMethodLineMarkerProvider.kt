package me.shedaniel.architectury.idea.gutter

import com.intellij.psi.PsiMethod

class CommonMethodLineMarkerProvider : AbstractCommonMethodLineMarkerProvider<PsiMethod>() {
    override val converter = PsiMethodConverter.JAVA
}
