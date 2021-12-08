plugins {
    `kotlin-dsl`
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
            implementationClass = "JvmWrapperPlugin"
        }
    }
}