package me.shedaniel.architectury.idea.kotlin.gutter

import me.shedaniel.architectury.idea.gutter.AbstractPlatformMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtNamedFunction

class PlatformFunctionLineMarkerProvider : AbstractPlatformMethodLineMarkerProvider<KtNamedFunction>() {
    override val converter = KotlinPsiMethodConverters.FUNCTION
}
