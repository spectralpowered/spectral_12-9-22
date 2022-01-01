plugins {
    id("org.openjfx.javafxplugin") version "0.0.10"
    application
}

dependencies {
    implementation(project(":spectral-common"))
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:_")
    implementation("no.tornado:tornadofx:_")
    implementation("net.java.dev.jna:jna:_")
    implementation("net.java.dev.jna:jna-platform:_")
}

javafx {
    version = rootProject.tasks.compileJava.get().targetCompatibility
    modules = listOf("javafx.base", "javafx.controls", "javafx.graphics", "javafx.swing")
}

application {
    mainClass.set("org.spectralpowered.launcher.Launcher")
}

tasks.named<JavaExec>("run") {
    dependsOn(project(":spectral-plugin").tasks.getByName("build"))
}

tasks.compileJava {
    dependsOn(project(":spectral-client").tasks.getByName("copyFullJar"))
    dependsOn(project(":spectral-cpp").tasks.build)
}