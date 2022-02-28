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