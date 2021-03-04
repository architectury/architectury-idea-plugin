package me.shedaniel.architectury.idea.util

import com.intellij.openapi.module.Module
import com.intellij.psi.PsiClass

enum class Platform(val id: String) {
    FABRIC("fabric"),
    FORGE("forge");

    fun isPlatformModule(module: Module): Boolean =
        when (this) {
            FABRIC -> module.isFabricModule
            FORGE -> module.isForgeModule
        }

    /**
     * Gets the name of the [clazz]'s implementation version for this platform.
     *
     * Example: `com.example.Example` -> `com.example.forge.ExampleImpl`
     */
    fun getImplementationName(clazz: PsiClass): String {
        val className = clazz.binaryName ?: error("Could not get binary name of $this")
        val parts = className.split('.')
        val head = parts.dropLast(1).joinToString(separator = ".")
        val tail = parts.last().replace("$", "")
        return "$head.$id.${tail}Impl"
    }
}
