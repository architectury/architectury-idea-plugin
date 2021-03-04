package me.shedaniel.architectury.idea.util

import com.intellij.openapi.module.Module
import com.intellij.psi.JavaPsiFacade

private fun Module.hasPackage(pkg: String): Boolean =
    JavaPsiFacade.getInstance(project).findPackage(pkg) != null

val Module.isCommonModule: Boolean get() = hasPackage("me.shedaniel.architectury.annotations")
val Module.isForgeModule: Boolean get() = hasPackage("net.minecraftforge")
val Module.isFabricModule: Boolean get() = hasPackage("net.fabricmc.api") && !isCommonModule
