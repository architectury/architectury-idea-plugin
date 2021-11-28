package dev.architectury.idea.kotlin.util

import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.asJava.LightClassUtil
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtPropertyAccessor

/**
 * Gets a "light class" version of this Kotlin function as a Java PSI method.
 */
fun KtNamedFunction.toPsiMethod(): PsiMethod? = LightClassUtil.getLightClassMethod(this)

/**
 * Gets a "light class" version of this Kotlin property accessor as a Java PSI method.
 */
fun KtPropertyAccessor.toPsiMethod(): PsiMethod? = LightClassUtil.getLightClassAccessorMethod(this)
