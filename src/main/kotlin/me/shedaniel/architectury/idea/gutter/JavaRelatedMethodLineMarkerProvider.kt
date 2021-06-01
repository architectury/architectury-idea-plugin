package me.shedaniel.architectury.idea.gutter

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

abstract class JavaRelatedMethodLineMarkerProvider : RelatedMethodLineMarkerProvider<PsiMethod>(PsiMethod::class.java) {
    override val PsiMethod.methodName: String get() = name
    override val PsiMethod.methodNameIdentifier: PsiElement get() = nameIdentifier!!
}
