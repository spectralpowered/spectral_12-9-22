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
            "/MDd",
            "/EHsc"
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
        linkTask.get().libs.from.addAll(listOf(
            "src/main/lib/PolyHook_2.lib"
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
        project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/spectral.dll").deleteRecursively()
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(project.buildDir.resolve("lib/main/release/spectral.dll"))
    into(project(":spectral-launcher").projectDir.resolve("src/main/resources/bin/"))
}

tasks.compileJava {
    finalizedBy(tasks.getByName("copyDll"))
    sourceCompatibility = rootProject.tasks.compileJava.get().sourceCompatibility
    targetCompatibility = rootProject.tasks.compileJava.get().targetCompatibility
    outputs.dir(jniHeaderDirectory)
    options.compilerArgumentProviders.add(CommandLineArgumentProvider { listOf(
        "-h", jniHeaderDirectory.get().asFile.canonicalPath
    ) })
}