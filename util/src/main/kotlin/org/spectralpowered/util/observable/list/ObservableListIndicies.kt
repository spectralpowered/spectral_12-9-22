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

import org.spectralpowered.util.observable.property.ObservablePropertyReference
import java.util.*

/**
 * An observable list of indexList that merely references another list.
 * Created by Kyle Escobar on 11/2/16.
 */
abstract class ObservableListIndicies<E>(val source: ObservableList<E>) : ObservableList<E> {
    val indexList = ArrayList<Int>()

    override val onAdd = HashSet<(E, Int) -> Unit>()
    override val onChange = HashSet<(E, E, Int) -> Unit>()
    override val onMove = HashSet<(E, Int, Int) -> Unit>()
    override val onUpdate = ObservablePropertyReference<ObservableList<E>>({ this }, { throw IllegalAccessException() })
    override val onReplace = HashSet<(ObservableList<E>) -> Unit>()
    override val onRemove = HashSet<(E, Int) -> Unit>()

    override fun replace(list: List<E>) {
        throw UnsupportedOperationException()
    }

    override fun set(index: Int, element: E): E {
        source[indexList.elementAt(index)] = element
        return element
    }

    override fun add(element: E): Boolean = source.add(element)
    override fun add(index: Int, element: E): Unit = source.add(indexList.elementAt(index), element)
    override fun addAll(elements: Collection<E>): Boolean = source.addAll(elements)
    override fun addAll(index: Int, elements: Collection<E>): Boolean = source.addAll(indexList.elementAt(index), elements)
    @Suppress("UNCHECKED_CAST")
    override fun remove(element: E): Boolean = source.remove(element)

    override fun move(fromIndex: Int, toIndex: Int) = source.move(indexList.elementAt(fromIndex), indexList.elementAt(toIndex))

    override fun removeAt(index: Int): E = source.removeAt(indexList.elementAt(index))
    @Suppress("UNCHECKED_CAST")
    override fun removeAll(elements: Collection<E>): Boolean = throw IllegalAccessException()

    override fun retainAll(elements: Collection<E>): Boolean = throw IllegalAccessException()
    override fun clear(): Unit = source.clear()

    override fun isEmpty(): Boolean = indexList.isEmpty()
    override fun contains(element: E): Boolean = indexList.contains(source.indexOf(element))
    override fun containsAll(elements: Collection<E>): Boolean = indexList.containsAll(elements.map { source.indexOf(it) })
    override fun listIterator(): MutableListIterator<E> = throw UnsupportedOperationException()
    override fun listIterator(index: Int): MutableListIterator<E> = throw UnsupportedOperationException()
    override fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
        val inner = indexList.iterator()
        override fun hasNext(): Boolean = inner.hasNext()
        override fun next(): E = source[inner.next()]

        override fun remove() {
            throw UnsupportedOperationException()
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> = throw UnsupportedOperationException()
    override fun get(index: Int): E = source[indexList.elementAt(index)]
    override fun indexOf(element: E): Int = indexList.indexOf(source.indexOf(element))
    override fun lastIndexOf(element: E): Int = indexList.lastIndexOf(source.lastIndexOf(element))
    override val size: Int get() = indexList.size
}