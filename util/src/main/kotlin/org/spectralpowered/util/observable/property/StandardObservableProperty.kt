package org.spectralpowered.util.observable.property

/**
 * Created by Kyle Escobar on 1/19/16.
 */
open class StandardObservableProperty<T>(
        initValue: T
) : ObservablePropertyBase<T>() {

    override var value: T = initValue
        set(value) {
            field = value
            update(value)
        }
}