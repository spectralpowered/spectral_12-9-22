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

import java.util.*

/**
 * Created by Kyle Escobar on 2/22/16.
 */
abstract class ObservablePropertyBase<T>() : MutableObservableProperty<T> {

    @Transient private val list = ArrayList<(T) -> Unit>()
    override val size: Int get() = list.size
    override fun contains(element: (T) -> Unit): Boolean = list.contains(element)
    override fun containsAll(elements: Collection<(T) -> Unit>): Boolean = list.containsAll(elements)
    override fun isEmpty(): Boolean = list.isEmpty()
    override fun clear() = list.clear()
    override fun iterator(): MutableIterator<(T) -> Unit> = list.iterator()
    override fun remove(element: (T) -> Unit): Boolean = list.remove(element)
    override fun removeAll(elements: Collection<(T) -> Unit>): Boolean = list.removeAll(elements)
    override fun retainAll(elements: Collection<(T) -> Unit>): Boolean = list.retainAll(elements)
    override fun add(element: (T) -> Unit): Boolean = list.add(element)
    override fun addAll(elements: Collection<(T) -> Unit>): Boolean = list.addAll(elements)

    fun update(value: T) {
        for (listener in list) {
            listener(value)
        }
    }
}