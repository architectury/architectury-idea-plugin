package me.shedaniel.architectury.idea.util

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass

enum class Platform(val id: String) {
    FABRIC("fabric"),
    FORGE("forge");

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

    fun isIn(project: Project): Boolean {
        val facade = JavaPsiFacade.getInstance(project)
        fun hasPackage(pkg: String): Boolean = facade.findPackage(pkg) != null

        return when (this) {
            FORGE -> hasPackage("net.minecraftforge")
            FABRIC -> hasPackage("net.fabricmc.api")
        }
    }
}
