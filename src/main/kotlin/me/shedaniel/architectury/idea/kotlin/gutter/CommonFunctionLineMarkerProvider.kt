package me.shedaniel.architectury.idea.kotlin.gutter

import me.shedaniel.architectury.idea.gutter.AbstractCommonMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtNamedFunction

class CommonFunctionLineMarkerProvider : AbstractCommonMethodLineMarkerProvider<KtNamedFunction>() {
    override val converter = KotlinPsiMethodConverters.FUNCTION
}
