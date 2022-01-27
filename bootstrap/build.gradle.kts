plugins {
    id("fr.stardustenterprises.rust.wrapper")
}

rust {
    command = "cargo"
    outputs = mutableMapOf("" to "spectral_bootstrap")
    outputDirectory = "bin/"
    profile = "release"
}