plugins {
    id("de.fayard.refreshVersions") version "0.23.0"
}

rootProject.name = "spectral"

module(":common")
module(":logger")
module(":launcher")
module(":client")

fun module(moduleName: String) {
    include(moduleName)
    project(moduleName).name = "spectral-${moduleName.substring(1)}"
}