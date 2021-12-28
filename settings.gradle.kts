import groovy.ant.FileNameFinder
import java.nio.file.Paths

plugins {
    id("de.fayard.refreshVersions") version "0.23.0"
}

rootProject.name = "spectral"

includeBuild("plugin-gradle")

module("common")
module("logger")
module("launcher")
module("client")
module("api")
module("plugin")
module("cpp", "natives/cpp")
module("jvm", "natives/jvm")
pluginModules("plugin")

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

fun pluginModules(module: String) {
    val relativePath = module.replace(":", "/")
    val rootDir = rootProject.projectDir.toPath().resolve(relativePath)
    if(rootDir.toFile().exists()) {
        val buildFiles = FileNameFinder().getFileNames("$rootDir", "**/*.gradle.kts")
        buildFiles.forEach { filename ->
            val buildFilePath = Paths.get(filename)
            val moduleDir = buildFilePath.parent
            val relativeDir = rootDir.relativize(moduleDir)
            val moduleName = "$relativeDir".replace(File.separator, ":")
            include(":spectral-$module:$moduleName")
        }
    }
}