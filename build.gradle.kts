plugins {
    id("org.jetbrains.intellij") version "0.7.2"
    kotlin("jvm") version "1.4.0" // Corresponds to IDEA 2020.3.2, see kt jars inside the ideaIC dep in IDEA
    id("org.jmailen.kotlinter") version "3.3.0"
}

group = "me.shedaniel"
version = "1.0.0"

repositories {
    mavenCentral()
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.3.2"
    setPlugins("java")
}

tasks {
    jar {
        from("COPYING", "COPYING.LESSER")
    }
}
