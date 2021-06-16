package me.shedaniel.architectury.idea.kotlin.gutter

import me.shedaniel.architectury.idea.gutter.PsiMethodConverter
import me.shedaniel.architectury.idea.kotlin.util.toPsiMethod
import org.jetbrains.kotlin.psi.KtNamedFunction

object KotlinPsiMethodConverters {
    val FUNCTION: PsiMethodConverter<KtNamedFunction> = PsiMethodConverter { it.toPsiMethod() }
}
