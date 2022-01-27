import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
}

dependencies {
    implementation(project(":common"))
    implementation(compose.desktop.currentOs)
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
}

compose.desktop {
    application {
        mainClass = "org.spectralpowered.launcher.Launcher"
        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "Spectral"
            packageVersion = "1.0.0"
        }
    }
}