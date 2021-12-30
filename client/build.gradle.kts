dependencies {
    implementation(project(":spectral-common"))
    implementation(project(":spectral-logger"))
    implementation(project(":spectral-jvm"))
    implementation(project(":spectral-api"))
    implementation(project(":spectral-plugin"))
    implementation(project(":runescape-api"))
    implementation("com.formdev:flatlaf:_")
    implementation("com.formdev:flatlaf-intellij-themes:_")
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

    register<Copy>("copyFullJar") {
        doFirst {
            file(project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/spectral.jar")).deleteRecursively()
        }

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(getByName("fullJar"))
        into(project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/"))
    }

    "build" {
        finalizedBy(getByName("copyFullJar"))
    }
}