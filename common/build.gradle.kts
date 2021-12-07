plugins {
    kotlin("multiplatform")
}

kotlin {
    val jvm = jvm()

    jvm.compilations["main"]
}