plugins {
    application
}

dependencies {
    implementation(project(":spectral-logger"))
    implementation(project(":spectral-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-cli:_")
}

application {
    mainClass.set("org.spectralpowered.launcher.Launcher")
}