package me.shedaniel.architectury.idea.kotlin.gutter

import com.intellij.psi.PsiElement
import me.shedaniel.architectury.idea.gutter.RelatedMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtNamedFunction

abstract class KotlinRelatedMethodLineMarkerProvider : RelatedMethodLineMarkerProvider<KtNamedFunction>(KtNamedFunction::class.java) {
    override val KtNamedFunction.methodName: String get() = name!!
    override val KtNamedFunction.methodNameIdentifier: PsiElement get() = nameIdentifier!!
}
