plugins {
    id("de.fayard.refreshVersions") version "0.23.0"
}

rootProject.name = "spectral"

module("common")
module("logger")
module("launcher")
module("client")
module("cpp", "natives/cpp")
module("jvm", "natives/jvm")

fun module(moduleName: String, modulePath: String? = null) {
    if(modulePath == null) {
        include(moduleName)
        project(":$moduleName").name = "spectral-$moduleName"
    } else {
        include(moduleName)
        project(":$moduleName").projectDir = file(modulePath)
        project(":$moduleName").name = "spectral-$moduleName"
    }
}