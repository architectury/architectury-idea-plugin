import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.intellij") version "1.0"
    kotlin("jvm") version "1.4.32" // Corresponds to IDEA 2021.2 EAP, see kt jars inside the ideaIC dep in IDEA
    id("org.jmailen.kotlinter") version "3.4.4"
}

group = "me.shedaniel"
version = "1.3.0"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("212-EAP-SNAPSHOT")
    plugins.set(listOf("java", "Kotlin"))
}

tasks {
    jar {
        from("COPYING", "COPYING.LESSER")
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
