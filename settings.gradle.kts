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

include(":util")
include(":common")
include(":launcher")
include(":engine")
include(":client")
include(":")