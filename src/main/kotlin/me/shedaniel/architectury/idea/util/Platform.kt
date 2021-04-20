package me.shedaniel.architectury.idea.util

import com.intellij.openapi.project.Project
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiClass
import org.jetbrains.annotations.PropertyKey

/**
 * An Architectury target platform.
 *
 * @property id a unique string ID for this platform from [PlatformIds]
 * @property translationKey a resource bundle key for the display name of this platform
 * @property fallbackPlatforms fallback platforms used for finding `@ExpectPlatform` implementation methods
 */
enum class Platform(
    val id: String,
    @PropertyKey(resourceBundle = BUNDLE) private val translationKey: String,
    val fallbackPlatforms: List<Platform> = emptyList()
) {
    FABRIC(PlatformIds.FABRIC, "platform.fabric"),
    FORGE(PlatformIds.FORGE, "platform.forge"),
    QUILT(PlatformIds.QUILT, "platform.quilt", listOf(FABRIC));

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
            QUILT -> hasPackage("org.quiltmc")
        }
    }

    override fun toString() = ArchitecturyBundle[translationKey]
}
