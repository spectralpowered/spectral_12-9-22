import de.fayard.refreshVersions.core.FeatureFlag.*

pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        google()
        gradlePluginPortal()
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
include(":common")
include(":util")
include(":bootstrap")
include(":client")
include(":natives")
include(":natives:memory")
include(":engine")
include(":api")