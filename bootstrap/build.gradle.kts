plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    command = "cargo"
    outputs = mutableMapOf("" to System.mapLibraryName("bootstrap"))
    outputDirectory = "bin/"
    profile = "release"
}

tasks {
    register<Copy>("copyDll") {
        val dir = project(":launcher").projectDir.resolve("build/resources/main/bin/")
        doFirst {
            dir.resolve("bootstrap.dll").deleteRecursively()
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(projectDir.resolve("target/release/bootstrap.dll"))
        into(dir)
    }

    build.get().finalizedBy("copyDll")
}