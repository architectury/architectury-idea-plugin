package me.shedaniel.architectury.idea.gutter

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
import com.intellij.psi.util.PsiTreeUtil
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import java.awt.event.MouseEvent
import javax.swing.Icon

abstract class RelatedMethodLineMarkerProvider<M : PsiElement>(
    private val parentType: Class<M>
) : LineMarkerProviderDescriptor(), GutterIconNavigationHandler<PsiElement> {
    protected abstract val tooltipTranslationKey: String
    protected abstract val navTitleTranslationKey: String

    protected abstract val M.relatedMethods: Set<PsiMethod>
    protected abstract val M.methodName: String
    protected abstract val M.methodNameIdentifier: PsiElement

    abstract override fun getIcon(): Icon

    @Suppress("UNCHECKED_CAST")
    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (parentType.isInstance(element) && (element as M).relatedMethods.isNotEmpty()) {
            return LineMarkerInfo(
                element.methodNameIdentifier,
                element.methodNameIdentifier.textRange,
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
        val method = PsiTreeUtil.getParentOfType(elt, parentType) ?: return
        val related = method.relatedMethods

        if (related.isNotEmpty()) {
            DefaultGutterIconNavigationHandler<PsiElement>(
                related,
                "<html>" + ArchitecturyBundle[navTitleTranslationKey, "<b>${method.methodName}</b>", related.size]
            ).navigate(e, elt)
        }
    }
}
