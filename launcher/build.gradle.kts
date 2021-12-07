import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform.getCurrentOperatingSystem
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithTests

fun KotlinMultiplatformExtension.setupNative(name: String, configure: KotlinNativeTargetWithTests<*>.() -> Unit): KotlinNativeTargetWithTests<*> {
    val os = getCurrentOperatingSystem()
    return when {
        os.isLinux -> linuxX64(name, configure)
        os.isWindows -> mingwX64(name, configure)
        os.isMacOsX -> macosX64(name, configure)
        else -> error("OS $os is not supported")
    }
}

plugins {
    kotlin("multiplatform")
    id("org.spectralpowered.gradle.jvm-wrapper")
}

kotlin {
    val jvm = jvm()

    jvm.compilations["main"].dependencies {
        implementation(kotlin("stdlib"))
    }

    val native = setupNative("native") {
        binaries {
            sharedLib()
        }

        compilations["main"].cinterops.create("jni") {
            // JDK is required here, JRE is not enough
            val javaHome = File(System.getenv("JAVA_HOME") ?: System.getProperty("java.home"))
            packageName = "org.spectralpowered.launcher"
            includeDirs(
                Callable { File(javaHome, "include") },
                Callable { File(javaHome, "include/darwin") },
                Callable { File(javaHome, "include/linux") },
                Callable { File(javaHome, "include/win32") }
            )
        }
    }

    val run by tasks.creating(JavaExec::class) {
        mainClass.set("org.spectralpowered.launcher.Launcher")
        group = "application"

        dependsOn(jvm.compilations.map { it.compileAllTaskName })
        dependsOn(native.compilations.map { it.compileAllTaskName })
        dependsOn(native.binaries.map { it.linkTaskName })

        systemProperty("spectralpowered", "true")

        doFirst {
            classpath(
                jvm.compilations["main"].output.allOutputs.files,
                configurations["jvmRuntimeClasspath"]
            )

            ///disable app icon on macOS
            systemProperty("java.awt.headless", "true")
            systemProperty("java.library.path", native.binaries.findSharedLib("debug")!!.outputDirectory)
        }
    }
}

jvmWrapper {
    linuxJvmUrl = "https://github.com/AdoptOpenJDK/openjdk16-binaries/releases/download/jdk-16.0.1%2B9/OpenJDK16U-jre_x64_linux_hotspot_16.0.1_9.tar.gz"
    macJvmUrl = "https://github.com/AdoptOpenJDK/openjdk16-binaries/releases/download/jdk-16.0.1%2B9/OpenJDK16U-jre_x64_mac_hotspot_16.0.1_9.tar.gz"
    windowsJvmUrl = "https://github.com/AdoptOpenJDK/openjdk16-binaries/releases/download/jdk-16.0.1%2B9/OpenJDK16U-jre_x64_windows_hotspot_16.0.1_9.zip"
}