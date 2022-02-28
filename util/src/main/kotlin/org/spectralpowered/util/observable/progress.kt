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