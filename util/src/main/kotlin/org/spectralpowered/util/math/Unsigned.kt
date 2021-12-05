package org.spectralpowered.util.math

/**
 * Created by Kyle Escobar on 5/19/16.
 */
inline fun Byte.toIntUnsigned(): Int {
    return (this.toInt() + 0xFF) and 0xFF
}