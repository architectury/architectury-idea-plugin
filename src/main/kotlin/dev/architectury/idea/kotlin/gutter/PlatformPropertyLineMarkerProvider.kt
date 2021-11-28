package dev.architectury.idea.kotlin.gutter

import dev.architectury.idea.gutter.AbstractPlatformMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtPropertyAccessor

class PlatformPropertyLineMarkerProvider : AbstractPlatformMethodLineMarkerProvider<KtPropertyAccessor>() {
    override val converter = KotlinPsiMethodConverters.PROPERTY_ACCESSOR
}
