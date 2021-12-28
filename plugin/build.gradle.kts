extra.apply {
    set("pluginsDir", rootProject.buildDir.path + "/plugins")
}

dependencies {
    api("org.pf4j:pf4j:_")
    annotationProcessor("org.pf4j:pf4j:_")
}

subprojects {
    dependencies {
        implementation(project(":spectral-api"))
        implementation(project(":spectral-logger"))
        implementation(project(":spectral-plugin"))
        annotationProcessor("org.pf4j:pf4j:_")
    }
}