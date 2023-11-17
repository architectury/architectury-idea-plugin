package dev.architectury.idea.util

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
 * @property identifyingPackage a package that is only present when a module is on this platform
 */
enum class Platform(
    val id: String,
    @PropertyKey(resourceBundle = BUNDLE) private val translationKey: String,
    private val identifyingPackage: String,
    val fallbackPlatforms: List<Platform> = emptyList()
) {
    FABRIC(PlatformIds.FABRIC, "platform.fabric", "net.fabricmc.api"),
    FORGE(PlatformIds.FORGE, "platform.forge", "net.minecraftforge.common"),
    // QUILT(PlatformIds.QUILT, "platform.quilt", "org.quiltmc", listOf(FABRIC)),
    NEOFORGE(PlatformIds.NEOFORGE, "platform.neoforge", "net.neoforged.neoforge")
    ;

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

    fun isIn(project: Project): Boolean =
        JavaPsiFacade.getInstance(project).findPackage(identifyingPackage) != null

    override fun toString() = ArchitecturyBundle[translationKey]
}
