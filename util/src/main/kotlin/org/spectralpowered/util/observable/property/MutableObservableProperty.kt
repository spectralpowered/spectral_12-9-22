package org.spectralpowered.util.observable.property

import kotlin.reflect.KProperty

/**
 * Created by Kyle Escobar on 5/31/2016.
 */
interface MutableObservableProperty<T> : ObservableProperty<T> {
    override var value: T

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, v: T) {
        value = v
    }
}