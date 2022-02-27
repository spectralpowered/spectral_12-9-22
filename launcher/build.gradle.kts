plugins {
    application
    id("fr.stardustenterprises.rust.importer")
    id("org.spectralpowered.gradle.plugin.update4j")
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

update4j {
    uri = "https://spectral.nyc3.digitaloceanspaces.com/client/live/"
    path = File(System.getProperty("user.home")).resolve(".spectral/").absolutePath
    output = "update4j/manifest.xml"
    file = "name=bin/spectral.jar"
}

tasks {
    named<JavaExec>("run") {
        dependsOn(project(":client").tasks.build)
    }
}