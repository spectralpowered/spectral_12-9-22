import org.gradle.internal.jvm.Jvm

plugins {
    `cpp-library`
    `java-library`
    idea
}

val jniImplemntation by configurations.creating

configurations.matching { it.name in listOf("cppCompile", "nativeLink", "nativeRuntime") }.all {
    extendsFrom(jniImplemntation)
}

val jniHeadersDirectory = layout.buildDirectory.dir("jniHeaders")

tasks.compileJava {
    outputs.dir(jniHeadersDirectory)
    options.compilerArgumentProviders.add(CommandLineArgumentProvider { listOf(
        "-h", jniHeadersDirectory.get().asFile.canonicalPath
    ) })
}

library {
    binaries.configureEach {
        compileTask.get().dependsOn(tasks.compileJava)
        compileTask.get().compilerArgs.addAll(listOf(
            "-I", project.projectDir.resolve("src/main/cpp").canonicalPath,
            "-I", project.projectDir.resolve("src/main/include").canonicalPath
        ))
        compileTask.get().compilerArgs.addAll(compileTask.get().targetPlatform.map {
            listOf("-I", "${Jvm.current().javaHome.canonicalPath}/include") + when {
                it.operatingSystem.isMacOsX -> listOf("-I", "${Jvm.current().javaHome.canonicalPath}/include/darwin")
                it.operatingSystem.isLinux -> listOf("-I", "${Jvm.current().javaHome.canonicalPath}/include/linux")
                else -> emptyList()
            }
        })
    }
}

sourceSets["main"].java {
    srcDir("src/main/cpp")
    srcDir("src/main/include")
    srcDir("src/main/lib")
}

idea {
    module {
        generatedSourceDirs.add(file("src/main/include"))
        generatedSourceDirs.add(file("src/main/lib"))
    }
}

tasks.jar {
    from(library.developmentBinary.flatMap { (it as CppSharedLibrary).linkFile })
}