plugins {
    id("org.jetbrains.compose")
    `java-library`
}

dependencies {
    implementation(project(":common"))
    implementation(project(":util"))
    implementation(project(":natives"))
    implementation(project(":engine"))
    implementation(compose.desktop.currentOs)
    implementation(compose.materialIconsExtended)
    implementation("com.formdev:flatlaf:_")
    implementation("com.formdev:flatlaf-intellij-themes:_")
}

tasks {
    named<Jar>("jar") {
        archiveBaseName.set("spectral")
        archiveVersion.set("")
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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