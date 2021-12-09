dependencies {
    implementation(project(":spectral-common"))
    implementation(project(":spectral-logger"))
}

tasks {
    register<Jar>("fatJar") {
        archiveBaseName.set("spectral")
        archiveVersion.set("")
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.runtimeClasspath.get().map {
            when(it.isDirectory) {
                true -> it
                false -> zipTree(it)
            }
        })
        with(jar.get())
    }

    register<Copy>("copyJar") {
        doFirst {
            project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/spectral.jar").also {
                if(it.exists()) it.deleteRecursively()
            }
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(getByName("fatJar"))
        into(project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/"))
    }
}