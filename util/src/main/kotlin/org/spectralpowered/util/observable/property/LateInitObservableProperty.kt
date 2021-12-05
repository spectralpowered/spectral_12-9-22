package org.spectralpowered.util.observable.property

/**
 * An observable that doesn't have to be set at its creation.
 * Created by Kyle Escobar on 2/11/16.
 */
open class LateInitObservableProperty<T : Any>() : ObservablePropertyBase<T>() {

    var internalValue: T? = null
    override var value: T
        get() = internalValue ?: throw throw IllegalAccessException("Value not set.")
        set(value) {
            internalValue = value
            update(value)
        }
}