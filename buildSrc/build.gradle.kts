plugins {
    kotlin("jvm") version "1.6.10"
    `maven-publish`
    `java-gradle-plugin`
}

group = "org.spectralpowered.gradle"
version = rootProject.version.toString()

repositories {
    mavenLocal()
    mavenCentral()
    google()
    maven(url = "https://jitpack.io/")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
}
