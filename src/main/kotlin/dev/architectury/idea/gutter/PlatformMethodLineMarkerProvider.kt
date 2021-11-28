package dev.architectury.idea.gutter

import com.intellij.psi.PsiMethod

class PlatformMethodLineMarkerProvider : AbstractPlatformMethodLineMarkerProvider<PsiMethod>() {
    override val converter = PsiMethodConverter.JAVA
}
