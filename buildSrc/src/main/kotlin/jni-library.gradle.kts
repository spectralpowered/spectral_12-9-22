import org.gradle.internal.jvm.Jvm

plugins {
    `cpp-library`
    `java-library`
    idea
}

val jniImplemntation by configurations.creating

configurations.matching { c -> listOf("cppCompile", "nativeLink", "nativeRuntime").any { c.name.startsWith(it) } }.all {
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
    source.from(file("src/main/cpp"))
    privateHeaders.from(file("src/main/cpp"))
    publicHeaders.from(file("src/main/include"))
    binaries.configureEach {
        val include = compileTask.get().targetPlatform.get().operatingSystem.let { when {
            it.isMacOsX || it.isLinux -> "-I"
            it.isWindows -> "/I "
            else -> throw UnsupportedOperationException("Current OS (${it.name} is not supported.")
        } }

        compileTask.get().dependsOn(tasks.compileJava)
        compileTask.get().compilerArgs.addAll(listOf("/MT"))
        compileTask.get().compilerArgs.addAll(listOf(
            include + project.projectDir.resolve("src/main/cpp").canonicalPath,
            include + project.projectDir.resolve("src/main/include").canonicalPath
        ))
        compileTask.get().compilerArgs.addAll(compileTask.get().targetPlatform.map {
            listOf(include + "${Jvm.current().javaHome.canonicalPath}/include") + when {
                it.operatingSystem.isMacOsX -> listOf(include + "${Jvm.current().javaHome.canonicalPath}/include/darwin")
                it.operatingSystem.isLinux -> listOf(include + "${Jvm.current().javaHome.canonicalPath}/include/linux")
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