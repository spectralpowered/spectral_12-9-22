import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipFile

plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    command = "cargo"
    outputs = mutableMapOf("" to System.mapLibraryName("bootstrap"))
    outputDirectory = "bin/"
    profile = "release"
    toolchain = "stable-x86_64-pc-windows-msvc"
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

    register("downloadJre") {
        val jreUrl = URL("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.2%2B8/OpenJDK17U-jre_x64_windows_hotspot_17.0.2_8.zip")
        val rootFolderName = "jdk-17.0.2+8-jre"

        val jreDir = file(System.getProperty("user.home")).resolve(".spectral/jre/")
        if(!jreDir.exists()) {
            jreDir.mkdirs()

            if(jreDir.listFiles()?.isEmpty() != false) {
                println("Downloading OpenJDK JRE 17.")
                val bytes = jreUrl.openConnection().getInputStream().readAllBytes()
                FileOutputStream(jreDir.resolve("jre.zip")).use {
                    it.write(bytes)
                }
                ZipFile(jreDir.resolve("jre.zip")).use { zip ->
                    zip.entries().asSequence().forEach { entry ->
                        val entryName = entry.name.replace("$rootFolderName/", "")
                        if(entry.isDirectory) {
                            if(entryName.isBlank() || entryName == rootFolderName) return@forEach
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

    build.get().dependsOn("downloadJre")
    build.get().finalizedBy("copyDll")
}