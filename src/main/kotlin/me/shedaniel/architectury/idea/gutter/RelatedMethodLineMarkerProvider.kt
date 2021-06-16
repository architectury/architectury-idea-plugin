package me.shedaniel.architectury.idea.gutter

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiTreeUtil
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import java.awt.event.MouseEvent
import javax.swing.Icon

abstract class RelatedMethodLineMarkerProvider<M : PsiNameIdentifierOwner> :
    LineMarkerProviderDescriptor(), GutterIconNavigationHandler<PsiElement> {
    protected abstract val tooltipTranslationKey: String
    protected abstract val navTitleTranslationKey: String

    protected abstract val converter: PsiMethodConverter<M>
    protected abstract val PsiMethod.relatedMethods: Set<PsiMethod>

    abstract override fun getIcon(): Icon

    @Suppress("UNCHECKED_CAST")
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        fun M.relatedMethods() = converter.toPsiMethod(this)?.relatedMethods ?: emptySet()

        if (converter.type.isInstance(element) && (element as M).relatedMethods().isNotEmpty()) {
            return LineMarkerInfo(
                element.nameIdentifier!!,
                element.nameIdentifier!!.textRange,
                icon,
                { ArchitecturyBundle[tooltipTranslationKey] },
                this,
                GutterIconRenderer.Alignment.LEFT,
                { ArchitecturyBundle[tooltipTranslationKey] }
            )
        }

        return null
    }

    override fun navigate(e: MouseEvent, elt: PsiElement) {
        val method = PsiTreeUtil.getParentOfType(elt, converter.type) ?: return
        val related = converter.toPsiMethod(method)?.relatedMethods ?: return

        if (related.isNotEmpty()) {
            DefaultGutterIconNavigationHandler<PsiElement>(
                related,
                "<html>" + ArchitecturyBundle[navTitleTranslationKey, "<b>${method.name}</b>", related.size]
            ).navigate(e, elt)
        }
    }
}
