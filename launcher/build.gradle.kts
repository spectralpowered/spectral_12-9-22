plugins {
    application
    id("fr.stardustenterprises.rust.importer")
}

dependencies {
    implementation(project(":common"))
    rustImport(project(":bootstrap"))
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
    implementation("net.java.dev.jna:jna:_")
    implementation("net.java.dev.jna:jna-platform:_")
}

application {
    mainClass.set("org.spectralpowered.launcher.Launcher")
    mainClassName = "org.spectralpowered.launcher.Launcher"
    applicationName = "Spectral"
}

tasks {
    named<JavaExec>("run") {
        dependsOn(project(":client").tasks.build)
    }
}