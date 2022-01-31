plugins {
    id("fr.stardustenterprises.rust.importer")
}

dependencies {
    implementation(project(":common"))
    rustImport(project(":bootstrap"))
    implementation("org.tinylog:tinylog-api-kotlin:_")
    implementation("org.tinylog:tinylog-impl:_")
    implementation("net.java.dev.jna:jna:_")
    implementation("net.java.dev.jna:jna-platform:_")
}