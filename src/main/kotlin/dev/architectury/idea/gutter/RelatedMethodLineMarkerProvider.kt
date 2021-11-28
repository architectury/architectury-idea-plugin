package dev.architectury.idea.gutter

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import dev.architectury.idea.util.ArchitecturyBundle
import javax.swing.Icon

abstract class RelatedMethodLineMarkerProvider<M : PsiElement> : LineMarkerProviderDescriptor() {
    protected abstract val tooltipTranslationKey: String
    protected abstract val navTitleTranslationKey: String

    protected abstract val converter: PsiMethodConverter<M>
    protected abstract val PsiMethod.relatedMethods: Set<PsiMethod>

    abstract override fun getIcon(): Icon

    @Suppress("UNCHECKED_CAST")
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (converter.type.isInstance(element)) {
            val related = converter.toPsiMethod(element as M)?.relatedMethods ?: emptySet()
            if (related.isEmpty()) return null

            val leaf = converter.getPreferredLeafElement(element)
            val name = converter.toNameIdentifierOwner(element).name
            return LineMarkerInfo(
                leaf,
                leaf.textRange,
                icon,
                { ArchitecturyBundle[tooltipTranslationKey] },
                DefaultGutterIconNavigationHandler(
                    related,
                    "<html>" + ArchitecturyBundle[navTitleTranslationKey, "<b>$name</b>", related.size]
                ),
                GutterIconRenderer.Alignment.LEFT,
                { ArchitecturyBundle[tooltipTranslationKey] }
            )
        }

        return null
    }
}
