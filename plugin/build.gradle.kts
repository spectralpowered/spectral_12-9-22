val pluginsDir by extra { file("${rootProject.buildDir}/plugins") }

dependencies {
    implementation(project(":spectral-common"))
    implementation(project(":spectral-logger"))
    api("org.pf4j:pf4j:_")
    annotationProcessor("org.pf4j:pf4j:_")
}

subprojects {
    dependencies {
        compileOnly(project(":spectral-api"))
        compileOnly(project(":spectral-logger"))
        compileOnly(project(":spectral-plugin"))
        annotationProcessor("org.pf4j:pf4j:_")
    }
}

tasks {
    register<Copy>("assemblePlugins") {
        dependsOn(subprojects.map { it.tasks.named("assemblePlugin") })
    }

    "build" {
        dependsOn(named("assemblePlugins"))
    }
}