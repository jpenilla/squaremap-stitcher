plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to "xyz.jpenilla.squaremapstitcher.AppKt")
        }
    }
    assemble {
        dependsOn(shadowJar)
    }
}
