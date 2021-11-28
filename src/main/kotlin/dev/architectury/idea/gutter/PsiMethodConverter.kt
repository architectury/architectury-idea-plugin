package dev.architectury.idea.gutter

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameIdentifierOwner

interface PsiMethodConverter<M : PsiElement> {
    val type: Class<M>
    fun toPsiMethod(element: M): PsiMethod?
    fun toNameIdentifierOwner(element: M): PsiNameIdentifierOwner

    fun getPreferredLeafElement(element: M): PsiElement =
        toNameIdentifierOwner(element).nameIdentifier!!

    companion object {
        val JAVA: PsiMethodConverter<PsiMethod> = PsiMethodConverter { it }

        inline operator fun <reified M : PsiNameIdentifierOwner> invoke(
            crossinline fn: (M) -> PsiMethod?
        ): PsiMethodConverter<M> = object : PsiMethodConverter<M> {
            override val type = M::class.java
            override fun toPsiMethod(element: M): PsiMethod? = fn(element)
            override fun toNameIdentifierOwner(element: M) = element
        }
    }
}
