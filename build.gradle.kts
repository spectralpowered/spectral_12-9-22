plugins {
    kotlin("jvm") version "1.6.0"
}

group = "org.spectralpowered"
version = "1.0.0"

println("------------------------------------------------------------------------------------")
println()
println(" __                 _             _     ___                               _ ")
println("/ _\\_ __   ___  ___| |_ _ __ __ _| |   / _ \\_____      _____ _ __ ___  __| |")
println("\\ \\| '_ \\ / _ \\/ __| __| '__/ _` | |  / /_)/ _ \\ \\ /\\ / / _ \\ '__/ _ \\/ _` |")
println("_\\ \\ |_) |  __/ (__| |_| | | (_| | | / ___/ (_) \\ V  V /  __/ | |  __/ (_| |")
println("\\__/ .__/ \\___|\\___|\\__|_|  \\__,_|_| \\/    \\___/ \\_/\\_/ \\___|_|  \\___|\\__,_|")
println("   |_|                                                                      ")
println()
println("Spectral Version: $version")
println("Gradle Version: ${gradle.gradleVersion}")
println("Java Version: ${System.getProperty("java.version")}")
println("Kotlin Version: ${kotlin.coreLibrariesVersion}")
println()
println("-----------------------------------------------------------------------------------")
println()

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenLocal()
        mavenCentral()
        maven(url = "https://jitpack.io/")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:_")
        implementation("org.jetbrains.kotlin:kotlin-reflect:_")
    }

    tasks {
        val javaVersion = JavaVersion.VERSION_16.toString()
        compileJava {
            targetCompatibility = javaVersion
            sourceCompatibility = javaVersion
        }
        compileKotlin {
            kotlinOptions {
                jvmTarget = javaVersion
                targetCompatibility = javaVersion
                sourceCompatibility = javaVersion
            }
        }
    }
}