package org.spectralpowered.util.async

/**
 * Created by Kyle Escobar on 6/1/16.
 */
interface AsyncInterface {
    fun sendToThread(action: () -> Unit)
}