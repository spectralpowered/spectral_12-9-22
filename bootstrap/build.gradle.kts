plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    command = "cargo"
    outputs = mutableMapOf("" to System.mapLibraryName("bootstrap"))
    outputDirectory = "bin/"
    profile = "release"
}