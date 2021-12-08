plugins {
    id("jni-library")
    kotlin("jvm")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:_")
    implementation("org.tinylog:tinylog-api-kotlin:_")
    api("org.tinylog:tinylog-impl:_")
}

tasks {
    val javaVersion = JavaVersion.VERSION_11.toString()

    compileKotlin {
        kotlinOptions {
            jvmTarget = javaVersion
            sourceCompatibility = javaVersion
            targetCompatibility = javaVersion
        }
    }

    compileJava {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}