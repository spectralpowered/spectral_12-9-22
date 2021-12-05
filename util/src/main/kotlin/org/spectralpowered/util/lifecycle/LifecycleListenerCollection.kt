package org.spectralpowered.util.lifecycle

/**
 * Listener for start and stop events on a lifecycle.
 * Created by Kyle Escobar on 6/1/16.
 */
class LifecycleListenerCollection(
        val collection: Collection<LifecycleListener>
) : LifecycleListener {
    override fun onStart() {
        collection.forEach { it.onStart() }
    }

    override fun onStop() {
        collection.forEach { it.onStop() }
    }
}