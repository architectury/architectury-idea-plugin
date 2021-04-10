plugins {
    id("org.jetbrains.intellij") version "0.7.2"
    kotlin("jvm") version "1.4.32" // Corresponds to IDEA 2021.1, see kt jars inside the ideaIC dep in IDEA
    id("org.jmailen.kotlinter") version "3.4.0"
}

group = "me.shedaniel"
version = "1.2.0"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2021.1"
    setPlugins("java")
}

tasks {
    jar {
        from("COPYING", "COPYING.LESSER")
    }
}
