plugins {
    kotlin("jvm")
}

tasks.wrapper {
    gradleVersion = "7.3"
}

allprojects {
    group = "org.spectralpowered"
    version = "1.0.0"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
        maven(url = "https://jitpack.io/")
    }
}

val ignoredProjects = listOf("bootstrap")
configure(allprojects.filter { it.name !in ignoredProjects }) {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))
    }

    tasks {
        val javaVersion = JavaVersion.VERSION_17.toString()

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