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

package org.spectralpowered.util.observable.list

import org.spectralpowered.util.observable.property.ObservableProperty

/**
 * Allows you to observe the changes to a list.
 * Created by Kyle Escobar on 9/7/2015.
 */
interface ObservableList<E> : MutableList<E> {

    val onAdd: MutableCollection<(E, Int) -> Unit>
    val onChange: MutableCollection<(E, E, Int) -> Unit>
    val onMove: MutableCollection<(E, Int, Int) -> Unit>
    val onUpdate: ObservableProperty<ObservableList<E>>
    val onReplace: MutableCollection<(ObservableList<E>) -> Unit>
    val onRemove: MutableCollection<(E, Int) -> Unit>

    fun move(fromIndex: Int, toIndex: Int)
    fun replace(list: List<E>)
    fun updateAt(index: Int) {
        this[index] = this[index]
    }

    fun update(element: E): Boolean {
        val index = indexOf(element)
        if (index != -1)
            updateAt(index)
        return index != -1
    }
}