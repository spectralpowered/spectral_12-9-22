plugins {
    kotlin("jvm") version "1.6.0"
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

configure(allprojects.filter { it.name !in listOf("spectral-cpp") }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib:_")
    }


    tasks {
        val javaVersion = JavaVersion.VERSION_11.toString()

        compileKotlin {
            kotlinOptions {
                jvmTarget = javaVersion
                sourceCompatibility = javaVersion
                targetCompatibility = javaVersion
            }
        }

        compileJava {
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }
}

tasks.wrapper {
    gradleVersion = "7.2"
    distributionType = Wrapper.DistributionType.ALL
}