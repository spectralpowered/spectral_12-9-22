import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks.wrapper {
    gradleVersion = "7.2"
}

allprojects {
    group = "org.spectralpowered"
    version = "1.0.0"

    repositories {
        mavenLocal()
        mavenCentral()
        google()
    }

    tasks.withType<KotlinCompile>().all {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_16.toString()
    }
}