package dev.architectury.idea.kotlin.gutter

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiTreeUtil
import dev.architectury.idea.gutter.PsiMethodConverter
import dev.architectury.idea.kotlin.util.toPsiMethod
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtPropertyAccessor

object KotlinPsiMethodConverters {
    val FUNCTION: PsiMethodConverter<KtNamedFunction> = PsiMethodConverter { it.toPsiMethod() }
    val PROPERTY_ACCESSOR: PsiMethodConverter<KtPropertyAccessor> = object : PsiMethodConverter<KtPropertyAccessor> {
        override val type = KtPropertyAccessor::class.java
        override fun toPsiMethod(element: KtPropertyAccessor): PsiMethod? = element.toPsiMethod()

        override fun toNameIdentifierOwner(element: KtPropertyAccessor): PsiNameIdentifierOwner =
            PsiTreeUtil.getParentOfType(element, KtProperty::class.java)!!

        override fun getPreferredLeafElement(element: KtPropertyAccessor): PsiElement =
            when {
                element.isGetter -> element.node.findChildByType(KtTokens.GET_KEYWORD)?.psi
                element.isSetter -> element.node.findChildByType(KtTokens.SET_KEYWORD)?.psi
                else -> null // shouldn't happen but it doesn't hurt to be careful
            } ?: super.getPreferredLeafElement(element)
    }
}
