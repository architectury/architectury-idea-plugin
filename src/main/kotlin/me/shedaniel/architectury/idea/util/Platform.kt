package me.shedaniel.architectury.idea.util

import com.intellij.openapi.module.Module

enum class Platform(val id: String) {
    FABRIC("fabric"),
    FORGE("forge");

    fun isPlatformModule(module: Module): Boolean =
        when (this) {
            FABRIC -> module.isFabricModule
            FORGE -> module.isForgeModule
        }
}
