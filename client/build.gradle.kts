dependencies {
    implementation(project(":common"))
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