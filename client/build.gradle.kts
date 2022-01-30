tasks.jar {
    archiveBaseName.set("spectral-client")
    archiveVersion.set("")
    archiveClassifier.set("")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map {
        if(it.isDirectory) it
        else zipTree(it)
    })
}