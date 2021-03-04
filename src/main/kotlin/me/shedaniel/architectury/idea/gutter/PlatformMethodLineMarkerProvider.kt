package me.shedaniel.architectury.idea.gutter

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.PsiMethod
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.commonMethods
import java.awt.event.MouseEvent
import javax.swing.Icon

class PlatformMethodLineMarkerProvider : LineMarkerProviderDescriptor(), GutterIconNavigationHandler<PsiIdentifier> {
    override fun getName(): String = "Platform implementation method line marker"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementingMethod

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiIdentifier>? {
        if (element is PsiIdentifier) {
            val parent = element.parent

            if (parent is PsiMethod && parent.nameIdentifier === element && parent.commonMethods.isNotEmpty()) {
                return LineMarkerInfo(
                    element,
                    element.textRange,
                    icon,
                    { ArchitecturyBundle["architectury.gutter.goToCommon"] },
                    this,
                    GutterIconRenderer.Alignment.LEFT,
                    { ArchitecturyBundle["architectury.gutter.goToCommon"] }
                )
            }
        }

        return null
    }

    override fun navigate(e: MouseEvent, elt: PsiIdentifier) {
        val method = elt.parent as? PsiMethod ?: return
        val commonMethods = method.commonMethods

        if (commonMethods.isNotEmpty()) {
            DefaultGutterIconNavigationHandler<PsiIdentifier>(
                commonMethods,
                "<html>" + ArchitecturyBundle["architectury.gutter.chooseCommon", "<b>${method.name}</b>", commonMethods.size]
            ).navigate(e, elt)
        }
    }
}
