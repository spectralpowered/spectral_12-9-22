package org.spectralpowered.util

/**
 * A simple interface that is missing in the Java standard library.
 * Anything that must be manually cleaned up (like things with event listeners) should use this.
 * Created by Kyle Escobar on 5/31/2016.
 */
interface Disposable {
    fun dispose()
}