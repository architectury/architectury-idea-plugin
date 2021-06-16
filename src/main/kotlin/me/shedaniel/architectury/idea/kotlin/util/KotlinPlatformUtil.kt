package me.shedaniel.architectury.idea.kotlin.util

import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.asJava.LightClassUtil
import org.jetbrains.kotlin.psi.KtNamedFunction

/**
 * Gets a "light class" version of this Kotlin function as a Java PSI method.
 */
fun KtNamedFunction.toPsiMethod(): PsiMethod? = LightClassUtil.getLightClassMethod(this)
