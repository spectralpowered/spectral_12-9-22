import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipFile

plugins {
    id("fr.stardustenterprises.rust.importer")
    application
}

dependencies {
    if(!project.projectDir.resolve("src/main/resources/bin/bootstrap.dll").exists()) {
        rustImport(project(":bootstrap"))
    }
    implementation(project(":common"))
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
    register("downloadJre") {
        val jreUrl =
            URL("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.2%2B8/OpenJDK17U-jre_x64_windows_hotspot_17.0.2_8.zip")
        val rootFolderName = "jdk-17.0.2+8-jre"

        val jreDir = file(System.getProperty("user.home")).resolve(".spectral/jre/")
        if (!jreDir.exists()) {
            jreDir.mkdirs()

            if (jreDir.listFiles()?.isEmpty() != false) {
                println("Downloading OpenJDK JRE 17.")
                val bytes = jreUrl.openConnection().getInputStream().readAllBytes()
                FileOutputStream(jreDir.resolve("jre.zip")).use {
                    it.write(bytes)
                }
                ZipFile(jreDir.resolve("jre.zip")).use { zip ->
                    zip.entries().asSequence().forEach { entry ->
                        val entryName = entry.name.replace("$rootFolderName/", "")
                        if (entry.isDirectory) {
                            if (entryName.isBlank() || entryName == rootFolderName) return@forEach
                            else jreDir.resolve(entryName).mkdirs()
                        } else {
                            zip.getInputStream(entry).use { input ->
                                jreDir.resolve(entryName).outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                        }
                    }
                }

                jreDir.resolve("jre.zip").deleteRecursively()
            }
        }
    }

    compileKotlin {
        val bootstrapDll = project.projectDir.resolve("src/main/resources/bin/bootstrap.dll")
        if(!bootstrapDll.exists()) {
            dependsOn(getByName("downloadJre"))
            dependsOn(project(":bootstrap").tasks.getByName("copyDll"))
        }
    }

    named<JavaExec>("run") {
        dependsOn(project(":client").tasks.build)
    }
}