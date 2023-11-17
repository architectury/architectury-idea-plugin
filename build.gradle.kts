import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "1.16.0"
    kotlin("jvm") version "1.7.21"
    id("org.jmailen.kotlinter") version "3.12.0"
}

// Corresponds to IDEA 2022.1, see KotlinVersion class in ideaIC/3rd-party-rt.jar
val kotlinVersion = "1.6.10"
val kotlinLanguageVersion = kotlinVersion.substringBeforeLast('.')

group = "dev.architectury"
version = "1.6.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", kotlinVersion))
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2022.1")
    plugins.set(listOf("java", "Kotlin"))
    updateSinceUntilBuild.set(false)
}

tasks {
    jar {
        from("COPYING", "COPYING.LESSER")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions {
            apiVersion = kotlinLanguageVersion
            languageVersion = kotlinLanguageVersion
        }
    }

    patchPluginXml {
        sinceBuild.set("221")
    }
}

kotlinter {
    disabledRules = arrayOf(
        "filename",
        "argument-list-wrapping",
    )
}
