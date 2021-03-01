package me.shedaniel.architectury.idea

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import java.awt.event.MouseEvent
import javax.swing.Icon

class CommonMethodLineMarkerProvider : LineMarkerProviderDescriptor(), GutterIconNavigationHandler<PsiIdentifier> {
    override fun getName(): String = "ExpectPlatform line marker"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementedMethod

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiIdentifier>? {
        if (element is PsiIdentifier) {
            val parent = element.parent

            if (parent is PsiMethod && parent.nameIdentifier === element && parent.platformMethods.isNotEmpty()) {
                return LineMarkerInfo(
                    element,
                    element.textRange,
                    icon,
                    { "Go to platform implementation" },
                    this,
                    GutterIconRenderer.Alignment.LEFT,
                    { "Go to platform implementation" }
                )
            }
        }

        return null
    }

    override fun navigate(e: MouseEvent, elt: PsiIdentifier) {
        val method = elt.parent as? PsiMethod ?: return
        val implementations = method.platformMethods

        if (implementations.isNotEmpty()) {
            DefaultGutterIconNavigationHandler<PsiIdentifier>(
                implementations,
                "<html>Choose Implementation of <b>${method.name}</b> (${implementations.size} methods found)"
            ).navigate(e, elt)
        }
    }
}
