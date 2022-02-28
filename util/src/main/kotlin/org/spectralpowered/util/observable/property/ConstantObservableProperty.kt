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

import kotlin.reflect.KProperty

/**
 * Created by Kyle Escobar on 12/2/16.
 */
class ConstantObservableProperty<T>(override val value: T) : ObservableProperty<T> {
    override val size: Int
        get() = 0

    override fun contains(element: (T) -> Unit): Boolean = false
    override fun containsAll(elements: Collection<(T) -> Unit>): Boolean = false
    override fun isEmpty(): Boolean = true
    override fun add(element: (T) -> Unit): Boolean = false
    override fun addAll(elements: Collection<(T) -> Unit>): Boolean = false
    override fun clear() {
    }

    override fun iterator(): MutableIterator<(T) -> Unit> = object : MutableIterator<(T) -> Unit> {
        override fun hasNext(): Boolean = false
        override fun next(): (T) -> Unit = throw UnsupportedOperationException("not implemented")
        override fun remove() {
        }
    }

    override fun remove(element: (T) -> Unit): Boolean = false
    override fun removeAll(elements: Collection<(T) -> Unit>): Boolean = false
    override fun retainAll(elements: Collection<(T) -> Unit>): Boolean = false
    override fun getValue(thisRef: Any?, prop: KProperty<*>): T {
        return super.getValue(thisRef, prop)
    }
}