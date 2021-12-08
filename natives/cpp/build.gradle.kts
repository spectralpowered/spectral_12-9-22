plugins {
    id("jni-library")
}

dependencies {
    jniImplemntation(files("src/main/lib/PolyHook_2.lib"))
}