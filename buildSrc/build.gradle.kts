plugins {
    kotlin("jvm") version "1.6.10"
    `kotlin-dsl`
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
    implementation("org.update4j:update4j:1.5.6")
}

tasks {
    register<Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    register<Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        from(project.tasks.getByName("javadoc"))
    }
}

gradlePlugin {
    plugins {
        create("update4j") {
            id = "org.spectralpowered.gradle.plugin.update4j"
            implementationClass = "org.spectralpowered.gradle.plugin.update4j.Update4jPlugin"
        }
    }
}
