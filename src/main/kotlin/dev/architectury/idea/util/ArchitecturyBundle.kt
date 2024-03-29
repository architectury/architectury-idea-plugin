package dev.architectury.idea.util

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

const val BUNDLE = "messages.Architectury"

object ArchitecturyBundle : DynamicBundle(BUNDLE) {
    @Nls
    operator fun get(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any?) =
        getMessage(key, *params)
}
