import org.gradle.internal.jvm.Jvm

plugins {
    `cpp-library`
    `java-library`
    idea
}

val jniImplementation by configurations.creating

configurations.matching { c -> listOf("cppCompile", "nativeLink", "nativeRuntime").any { c.name.startsWith(it) } }.all {
    extendsFrom(jniImplementation)
}

val jniHeaderDirectory = layout.buildDirectory.dir("jniHeaders")

tasks.compileJava {
    sourceCompatibility = rootProject.tasks.compileJava.get().sourceCompatibility
    targetCompatibility = rootProject.tasks.compileJava.get().targetCompatibility
    outputs.dir(jniHeaderDirectory)
    options.compilerArgumentProviders.add(CommandLineArgumentProvider { listOf(
        "-h", jniHeaderDirectory.get().asFile.canonicalPath
    ) })
}

library {
    baseName.set("spectral")
    source.from("src/main/cpp")
    privateHeaders.from("src/main/cpp")
    publicHeaders.from("src/main/include")
    binaries.configureEach {
        this as CppSharedLibrary
        compileTask.get().dependsOn(tasks.compileJava)
        compileTask.get().compilerArgs.addAll(compileTask.get().toolChain.map { listOf(
            "/std:c++17",
            "/MD",
            "/MDd"
        ) })
        compileTask.get().compilerArgs.addAll(listOf(
            "/I${jniHeaderDirectory.get().asFile.canonicalPath}",
            "/I${Jvm.current().javaHome.canonicalPath}/include",
             "/I${Jvm.current().javaHome.canonicalPath}/include/win32",
             "/I${Jvm.current().javaHome.canonicalPath}/include/win32/bridge",
        ))
        linkTask.get().linkerArgs.addAll(listOf(
            "/LIBPATH:src/main/lib",
            "/LIBPATH:${Jvm.current().javaHome.canonicalPath}/lib"
        ))
    }
}

sourceSets["main"].java {
    srcDir("src/main/cpp")
    srcDir("src/main/include")
    srcDir("src/main/lib")
}

idea {
    module {
        generatedSourceDirs.addAll(listOf(
            "src/main/include",
            "src/main/lib"
        ).map { file(it) })
    }
}

tasks.register<Copy>("copyDll") {
    doFirst {
        project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/spectral.dll").also {
            if(it.exists()) it.deleteRecursively()
        }
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(tasks.getByName("linkRelease"))
    into(project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/"))
}