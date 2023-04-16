import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "1.13.3"
    kotlin("jvm") version "1.8.20"
    id("org.jmailen.kotlinter") version "3.13.0"
}

// Corresponds to IDEA 2023.1, see KotlinVersion class in ideaIC/3rd-party-rt.jar
val kotlinVersion = "1.8.0"
val kotlinLanguageVersion = kotlinVersion.substringBeforeLast('.')

group = "dev.architectury"
version = "1.6.3"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.1")
    plugins.set(listOf("java", "Kotlin"))
    updateSinceUntilBuild.set(false)
}

tasks {
    jar {
        from("COPYING", "COPYING.LESSER")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
        kotlinOptions {
            apiVersion = kotlinLanguageVersion
            languageVersion = kotlinLanguageVersion
        }
    }

    patchPluginXml {
        sinceBuild.set("231")
    }
}

kotlinter {
    disabledRules = arrayOf(
        "filename",
        "argument-list-wrapping",
    )
}
