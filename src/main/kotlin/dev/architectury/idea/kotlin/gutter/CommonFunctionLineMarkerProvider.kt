package dev.architectury.idea.kotlin.gutter

import dev.architectury.idea.gutter.AbstractCommonMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtNamedFunction

class CommonFunctionLineMarkerProvider : AbstractCommonMethodLineMarkerProvider<KtNamedFunction>() {
    override val converter = KotlinPsiMethodConverters.FUNCTION
}
