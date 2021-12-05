package org.spectralpowered.util.observable.list

import org.spectralpowered.util.lifecycle.LifecycleConnectable
import org.spectralpowered.util.lifecycle.LifecycleListener
import org.spectralpowered.util.observable.property.bind

/**
 * Extensions that allow using ObservablePropertys with the LifecycleConnectable.
 * Created by Kyle Escobar on 6/1/16.
 */

fun <T> LifecycleConnectable.bind(observable: ObservableList<T>, listener: (ObservableList<T>) -> Unit) {
    bind(observable.onUpdate, listener)
}

fun <T> LifecycleConnectable.bind(observable: ObservableList<T>, listenerSet: ObservableListListenerSet<T>) {
    connect(object : LifecycleListener {
        override fun onStart() {
            observable.addListenerSet(listenerSet)
        }

        override fun onStop() {
            observable.removeListenerSet(listenerSet)
        }
    })
}