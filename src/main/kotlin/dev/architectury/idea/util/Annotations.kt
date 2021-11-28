package dev.architectury.idea.util

import com.intellij.psi.PsiAnnotation
import com.intellij.psi.PsiArrayInitializerMemberValue
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLiteralValue
import com.intellij.psi.PsiReferenceExpression
import com.intellij.psi.PsiVariable

object Annotations {
    private const val BARE_ANNOTATION_PROPERTY = "value"

    fun getStrings(annotation: PsiAnnotation, key: String): List<String> {
        val valueElement = annotation.findAttributeValue(key) ?: run {
            if (key == BARE_ANNOTATION_PROPERTY) {
                annotation.findAttributeValue(null)
            }

            null
        } ?: return emptyList()

        return when (valueElement) {
            is PsiArrayInitializerMemberValue -> valueElement.initializers.mapNotNull(::getStringConstant)
            else -> getStringConstant(valueElement)?.let { listOf(it) } ?: emptyList()
        }
    }

    private fun getStringConstant(element: PsiLiteralValue): String =
        element.value.toString()

    private fun getStringConstant(element: PsiElement): String? =
        when (element) {
            is PsiLiteralValue -> getStringConstant(element)
            is PsiReferenceExpression -> element.resolve()?.let(::getStringConstant)
            is PsiVariable -> element.computeConstantValue()?.toString()
            else -> null
        }
}
