plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    kotlin("jvm") version "1.6.0"
    id("com.gradle.plugin-publish") version "0.13.0"
}

repositories {
    mavenLocal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("spectral-plugin-gradle") {
            id = "org.spectralpowered.plugin"
            version = "0.0.1"
            implementationClass = "org.spectralpowered.plugin.gradle.SpectralGradlePlugin"
        }
    }
}