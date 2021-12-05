package org.spectralpowered.util.lifecycle

/**
 * Used to interface with different components that have different lifecycles.
 * Created by Kyle Escobar on 6/1/16.
 */
interface LifecycleConnectable {
    fun connect(listener: LifecycleListener)
}