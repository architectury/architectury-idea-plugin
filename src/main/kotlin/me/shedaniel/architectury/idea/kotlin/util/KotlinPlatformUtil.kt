package me.shedaniel.architectury.idea.kotlin.util

import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.util.commonMethods
import me.shedaniel.architectury.idea.util.platformMethods
import org.jetbrains.kotlin.asJava.LightClassUtil
import org.jetbrains.kotlin.psi.KtNamedFunction

/**
 * Gets a "light class" version of this Kotlin function as a Java PSI method.
 */
fun KtNamedFunction.toPsiMethod(): PsiMethod? = LightClassUtil.getLightClassMethod(this)

/**
 * The common declarations corresponding to this platform method.
 */
val KtNamedFunction.commonMethods: Set<PsiMethod>
    get() = toPsiMethod()?.commonMethods ?: emptySet()

/**
 * The platform implementations of this common method.
 */
val KtNamedFunction.platformMethods: Set<PsiMethod>
    get() = toPsiMethod()?.platformMethods ?: emptySet()
