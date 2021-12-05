package org.spectralpowered.util.lifecycle

/**
 * Listener for start and stop events on a lifecycle.
 * Created by Kyle Escobar on 6/1/16.
 */
interface LifecycleListener {
    fun onStart()
    fun onStop()
}