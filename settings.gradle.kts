import de.fayard.refreshVersions.core.FeatureFlag.*

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
        maven(url = "https://jitpack.io/")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.8"
    id("de.fayard.refreshVersions") version "0.30.1"
}

refreshVersions {
    featureFlags {
        enable(LIBS)
    }
}

rootProject.name = "spectral"

include(":launcher")
include(":launcher:updater")
include(":common")
include(":util")
include(":bootstrap")
include(":client")
include(":natives")
include(":natives:memory")
include(":natives:offset")
include(":engine")
include(":api")
include(":plugin")