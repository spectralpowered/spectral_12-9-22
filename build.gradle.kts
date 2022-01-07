plugins {
    kotlin("jvm") version "1.6.10"
}

group = "org.spectralpowered"
version = "1.0.0"

println("------------------------------------------------------------------------------------")
println("Spectral Version: $version")
println("Gradle Version: ${gradle.gradleVersion}")
println("Java Version: ${System.getProperty("java.version")}")
println("-----------------------------------------------------------------------------------")
println()

allprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://jitpack.io/")
    }
}

