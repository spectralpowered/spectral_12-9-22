pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://repo.nokee.dev/release") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id.startsWith("dev.nokee.")) {
                useModule("${requested.id.id}:${requested.id.id}.gradle.plugin:0.4.0")
            }
        }
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.23.0"
}

rootProject.name = "spectral"

fun module(path: String) {
    val index = path.lastIndexOf('/')
    val name = path.substring(index + 1)
    include(name)
    project(":$name").projectDir = file(path)
}

module("common")
module("util")
module("logger")
module("launcher")
module("client")
module("spectral-cpp")
module("natives")
