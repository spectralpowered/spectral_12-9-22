plugins {
    id("de.fayard.refreshVersions") version "0.30.1"
    id("com.gradle.enterprise") version "3.8"
}

rootProject.name = "spectral-powered"

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishOnFailure()
    }
}