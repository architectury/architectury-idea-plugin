package dev.architectury.idea.kotlin.gutter

import dev.architectury.idea.gutter.AbstractPlatformMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtNamedFunction

class PlatformFunctionLineMarkerProvider : AbstractPlatformMethodLineMarkerProvider<KtNamedFunction>() {
    override val converter = KotlinPsiMethodConverters.FUNCTION
}
