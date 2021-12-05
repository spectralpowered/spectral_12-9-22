package org.spectralpowered.util.observable

import org.spectralpowered.util.async.doUiThread
import org.spectralpowered.util.observable.property.MutableObservableProperty


fun <T> (() -> T).captureProgress(observable: MutableObservableProperty<Boolean>): (() -> T) {
    return {
        doUiThread {
            observable.value = true
        }
        val result = this()
        doUiThread {
            observable.value = false
        }
        result
    }
}

@JvmName("captureProgressInt")
fun <T> (() -> T).captureProgress(observable: MutableObservableProperty<Int>): (() -> T) {
    return {
        doUiThread {
            observable.value++
        }
        val result = this()
        doUiThread {
            observable.value--
        }
        result
    }
}