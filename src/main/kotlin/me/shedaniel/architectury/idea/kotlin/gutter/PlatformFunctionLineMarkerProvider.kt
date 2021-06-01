package me.shedaniel.architectury.idea.kotlin.gutter

import com.intellij.codeInsight.daemon.DefaultGutterIconNavigationHandler
import com.intellij.codeInsight.daemon.GutterIconNavigationHandler
import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import me.shedaniel.architectury.idea.kotlin.util.commonMethods
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import org.jetbrains.kotlin.psi.KtNamedFunction
import java.awt.event.MouseEvent
import javax.swing.Icon

class PlatformFunctionLineMarkerProvider : LineMarkerProviderDescriptor(), GutterIconNavigationHandler<PsiElement> {
    override fun getName(): String = "Platform implementation method line marker (Kotlin)"
    override fun getIcon(): Icon = AllIcons.Gutter.ImplementingMethod

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<PsiElement>? {
        if (element is KtNamedFunction && element.commonMethods.isNotEmpty()) {
            return LineMarkerInfo(
                element.nameIdentifier!!,
                element.nameIdentifier!!.textRange,
                icon,
                { ArchitecturyBundle["architectury.gutter.goToCommon"] },
                this,
                GutterIconRenderer.Alignment.LEFT,
                { ArchitecturyBundle["architectury.gutter.goToCommon"] }
            )
        }

        return null
    }

    override fun navigate(e: MouseEvent, elt: PsiElement) {
        val fn = PsiTreeUtil.getParentOfType(elt, KtNamedFunction::class.java) ?: return
        val commonMethods = fn.commonMethods

        if (commonMethods.isNotEmpty()) {
            DefaultGutterIconNavigationHandler<PsiElement>(
                commonMethods,
                "<html>" + ArchitecturyBundle["architectury.gutter.chooseCommon", "<b>${fn.name}</b>", commonMethods.size]
            ).navigate(e, elt)
        }
    }
}
