package org.spectralpowered.util.files

import java.io.File

/**
 * Created by Kyle Escobar on 6/2/16.
 */
fun File.child(name: String): File {
    return File(this, name)
}