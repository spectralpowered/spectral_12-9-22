/*
 * Copyright (C) 2021 Spectral Powered <Kyle Escobar>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spectralpowered.util.observable.property

import org.spectralpowered.util.Disposable

/**
 * Created by Kyle Escobar on 4/5/16.
 */
class ObservableObservablePropertyOpt<T>(initialObservable: MutableObservableProperty<T>? = null) : ObservablePropertyBase<T?>(),
    Disposable {
    val myListener: (T) -> Unit = {
        super.update(it)
    }

    var observable: ObservableProperty<T>? = null
        set(value) {
            field?.remove(myListener)
            field = value
            field?.add(myListener)
            super.update(value?.value)
        }

    init {
        observable = initialObservable
    }

    override var value: T?
        get() = observable?.value
        set(value) {
            val obs = observable
            if (obs is MutableObservableProperty && value != null) {
                obs.value = value
            } else {
                throw IllegalAccessException()
            }
        }

    override fun dispose() {
        observable?.remove(myListener)
    }

}