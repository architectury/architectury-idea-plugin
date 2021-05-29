package me.shedaniel.architectury.idea.util

enum class AnnotationType(private val annotations: Set<String>) : Set<String> by annotations {
    EXPECT_PLATFORM(
        "dev.architectury.injectables.annotations.ExpectPlatform",
        "me.shedaniel.architectury.annotations.ExpectPlatform",
        "me.shedaniel.architectury.ExpectPlatform"
    ),
    TRANSFORMED_EXPECT_PLATFORM(
        "dev.architectury.injectables.annotations.ExpectPlatform.Transformed",
        "me.shedaniel.architectury.annotations.ExpectPlatform.Transformed"
    ),
    PLATFORM_ONLY(
        "dev.architectury.injectables.annotations.PlatformOnly",
        "me.shedaniel.architectury.annotations.PlatformOnly"
    ),
    ;

    constructor(vararg annotations: String) : this(annotations.toSet())
}
