plugins {
    kotlin("jvm") version "1.6.0"
    id("com.gradle.plugin-publish") version "0.18.0"
    `java-gradle-plugin`
}

repositories {
    mavenLocal()
    mavenCentral()
}

gradlePlugin {
    plugins {
        create("jvm-wrapper") {
            id = "org.spectralpowered.gradle.jvm-wrapper"
            implementationClass = "org.spectralpowered.gradle.JvmWrapperPlugin"
        }
    }
}
