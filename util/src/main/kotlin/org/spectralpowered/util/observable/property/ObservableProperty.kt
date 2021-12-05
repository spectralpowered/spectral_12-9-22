package org.spectralpowered.util.observable.property

import org.spectralpowered.util.invokeAll
import kotlin.reflect.KProperty

/**
 * Created by Kyle Escobar on 1/28/2016.
 */
interface ObservableProperty<T> : MutableCollection<(T) -> Unit> {

    /**
     * The current value of the observable property.
     */
    val value: T

    operator fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        return value
    }

    fun update() = this.invokeAll(value)
}