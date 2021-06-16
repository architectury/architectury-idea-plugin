package me.shedaniel.architectury.idea.gutter

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod

interface PsiMethodConverter<M : PsiElement> {
    val type: Class<M>
    fun toPsiMethod(element: M): PsiMethod?

    companion object {
        val JAVA: PsiMethodConverter<PsiMethod> = PsiMethodConverter { it }

        inline operator fun <reified M : PsiElement> invoke(crossinline fn: (M) -> PsiMethod?): PsiMethodConverter<M> =
            object : PsiMethodConverter<M> {
                override val type = M::class.java
                override fun toPsiMethod(element: M): PsiMethod? = fn(element)
            }
    }
}
