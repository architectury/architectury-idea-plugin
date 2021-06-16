package me.shedaniel.architectury.idea.kotlin.gutter

import me.shedaniel.architectury.idea.gutter.AbstractCommonMethodLineMarkerProvider
import org.jetbrains.kotlin.psi.KtPropertyAccessor

class CommonPropertyLineMarkerProvider : AbstractCommonMethodLineMarkerProvider<KtPropertyAccessor>() {
    override val converter = KotlinPsiMethodConverters.PROPERTY_ACCESSOR
}
