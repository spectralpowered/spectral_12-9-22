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

/**
 *
 * Created by Kyle Escobar on 8/19/2017.
 */
class VirtualMutableObservableProperty<T>(
        val getterFun: () -> T,
        val setterFun: (T) -> Unit,
        val event: MutableCollection<(T) -> Unit>
) : EnablingMutableCollection<(T) -> Unit>(), MutableObservableProperty<T> {

    override var value: T
        get() = getterFun()
        set(value) {
            setterFun(value)
        }

    val listener = { t: T -> update() }

    override fun enable() {
        event.add(listener)
    }

    override fun disable() {
        event.remove(listener)
    }
}
