plugins {
    `java-library`
}

dependencies {
    implementation(project(":common"))
    implementation(project(":util"))
    implementation(project(":engine"))
    implementation(project(":natives"))
    implementation(project(":api"))
    implementation(project(":plugin"))
    implementation("com.formdev:flatlaf:_")
    implementation("com.formdev:flatlaf-intellij-themes:_")
}

tasks {
    named<Jar>("jar") {
        archiveBaseName.set("spectral")
        archiveVersion.set("")
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes["Main-Class"] = "my.main.class"
        }
        from(configurations.runtimeClasspath.get().map {
            if (it.isDirectory) it
            else zipTree(it)
        })
    }

    register<Copy>("copyJar") {
        val dir = project(":launcher").projectDir.resolve("src/main/resources/bin/")
        doFirst {
            dir.resolve("spectral.jar").deleteRecursively()
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(this@tasks.jar)
        into(dir)
    }

    jar.get().finalizedBy("copyJar")
}