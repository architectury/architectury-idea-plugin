package dev.architectury.idea.kotlin.gutter

import dev.architectury.idea.gutter.AbstractCommonMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtPropertyAccessor

class CommonPropertyLineMarkerProvider : AbstractCommonMethodLineMarkerProvider<KtPropertyAccessor>() {
    override val converter = KotlinPsiMethodConverters.PROPERTY_ACCESSOR
}
