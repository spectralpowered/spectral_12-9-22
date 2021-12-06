plugins {
    id("org.openjfx.javafxplugin") version "0.0.10"
}

dependencies {
    implementation(project(":common"))
    implementation(project(":natives"))
    implementation("com.formdev:flatlaf:_")
    implementation("com.formdev:flatlaf-intellij-themes:_")
}

javafx {
    version = "16"
    modules = listOf("javafx.base", "javafx.controls", "javafx.graphics", "javafx.swing")
}

tasks {
    register<Jar>("fullJar") {
        group = "build"
        archiveBaseName.set("spectral")
        archiveClassifier.set("")
        archiveVersion.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.compileClasspath.get().map {
            if(it.isDirectory) it
            else zipTree(it)
        })
        from(sourceSets["main"].output)
    }

    register<Copy>("copyJar") {
        doFirst {
            project(":launcher").projectDir.resolve("src/main/resources/bin/spectral.jar").deleteRecursively()
        }
        from(getByName("fullJar"))
        into(project(":launcher").projectDir.resolve("src/main/resources/bin/"))
    }
}