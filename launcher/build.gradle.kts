plugins {
    application
}

dependencies {
    implementation(project(":common"))
    implementation("com.formdev:flatlaf:_")
    implementation("com.formdev:flatlaf-intellij-themes:_")
    implementation("net.coobird:thumbnailator:_")
    implementation("net.java.dev.jna:jna:_")
    implementation("net.java.dev.jna:jna-platform:_")
}

application {
    mainClass.set("org.spectralpowered.launcher.Launcher")
}

tasks {
    named<JavaExec>("run") {
        group = "spectral"
    }

    register<Jar>("fullJar") {
        group = "build"
        archiveBaseName.set("spectral")
        archiveClassifier.set("")
        archiveVersion.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest {
            attributes("Main-Class" to application.mainClass)
        }
        from(configurations.compileClasspath.get().map {
            if(it.isDirectory) it
            else zipTree(it)
        })
        from(sourceSets["main"].output)
    }

    register<Copy>("copyDll") {
        doFirst {
            project.projectDir.resolve("src/main/resources/bin/spectral.dll").deleteRecursively()
        }
        from(project(":spectral-cpp").projectDir.resolve("build/spectral.dll"))
        into(project.projectDir.resolve("src/main/resources/bin/"))
    }

    compileKotlin {
        dependsOn(project(":client").tasks.getByName("copyJar"))
        dependsOn(getByName("copyDll"))
    }
}