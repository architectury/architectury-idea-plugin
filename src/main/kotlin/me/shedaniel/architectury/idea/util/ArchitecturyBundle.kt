package me.shedaniel.architectury.idea.util

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.Architectury"

object ArchitecturyBundle : DynamicBundle(BUNDLE) {
    operator fun get(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any?) =
        getMessage(key, *params)
}
