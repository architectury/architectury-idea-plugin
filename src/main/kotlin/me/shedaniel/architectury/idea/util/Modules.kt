package me.shedaniel.architectury.idea.util

import com.intellij.openapi.module.Module
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope

/**
 * True if this module is a common (cross-platform) module, false otherwise.
 */
val Module.isCommon: Boolean
    get() {
        val facade = JavaPsiFacade.getInstance(project)
        val scope = GlobalSearchScope.moduleWithLibrariesScope(this)
        return AnnotationType.EXPECT_PLATFORM.any { facade.findClass(it, scope) != null }
    }
