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

import org.spectralpowered.util.invokeAll
import org.spectralpowered.util.observable.property.ObservablePropertyReference
import java.util.*

/**
 * Allows you to observe the changes to a list.
 * Created by Kyle Escobar on 9/7/2015.
 */
class ObservableListWrapper<E>(
        val collection: MutableList<E> = mutableListOf()
) : ObservableList<E> {

    override val onAdd = HashSet<(E, Int) -> Unit>()
    override val onChange = HashSet<(E, E, Int) -> Unit>()
    override val onMove = HashSet<(E, Int, Int) -> Unit>()
    override val onUpdate = ObservablePropertyReference<ObservableList<E>>({ this@ObservableListWrapper }, { replace(it) })
    override val onReplace = HashSet<(ObservableList<E>) -> Unit>()
    override val onRemove = HashSet<(E, Int) -> Unit>()

    override fun set(index: Int, element: E): E {
        val old = collection[index]
        collection[index] = element
        onChange.invokeAll(old, element, index)
        onUpdate.invokeAll(this)
        return element
    }

    override fun add(element: E): Boolean {
        val result = collection.add(element)
        val index = collection.size - 1
        if (result) {
            onAdd.invokeAll(element, index)
            onUpdate.invokeAll(this)
        }
        return result
    }

    override fun add(index: Int, element: E) {
        collection.add(index, element)
        onAdd.invokeAll(element, index)
        onUpdate.invokeAll(this)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        var index = collection.size
        for (e in elements) {
            collection.add(e)
            onAdd.invokeAll(e, index)
            index++
        }
        onUpdate.invokeAll(this)
        return true
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        var currentIndex = index
        for (e in elements) {
            collection.add(currentIndex, e)
            onAdd.invokeAll(e, currentIndex)
            currentIndex++
        }
        onUpdate.invokeAll(this)
        return true
    }

    @Suppress("UNCHECKED_CAST")
    override fun remove(element: E): Boolean {
        val index = indexOf(element)
        if (index == -1) return false
        collection.removeAt(index)
        onRemove.invokeAll(element, index)
        onUpdate.invokeAll(this)
        return true
    }

    override fun removeAt(index: Int): E {
        val element = collection.removeAt(index)
        onRemove.invokeAll(element, index)
        onUpdate.invokeAll(this)
        return element
    }

    @Suppress("UNCHECKED_CAST")
    override fun removeAll(elements: Collection<E>): Boolean {
        for (e in elements) {
            remove(e)
        }
        return true
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun clear() {
        collection.clear()
        onReplace.invokeAll(this)
        onUpdate.invokeAll(this)
    }

    override fun isEmpty(): Boolean = collection.isEmpty()
    override fun contains(element: E): Boolean = collection.contains(element)
    override fun containsAll(elements: Collection<E>): Boolean = collection.containsAll(elements)
    override fun listIterator(): MutableListIterator<E> = throw UnsupportedOperationException()
    override fun listIterator(index: Int): MutableListIterator<E> = throw UnsupportedOperationException()
    /**
     * WARNING:
     * This iterator MAY have issues when it removes things, because there is no way to know if the iterator is done with its work.
     * It will not call onUpdate, and calls onRemove while iterating.
     *
     * YOU HAVE BEEN WARNED.
     */
    override fun iterator(): MutableIterator<E> = object : MutableIterator<E> {
        val inner = collection.iterator()
        var lastIndex: Int = -1
        var lastElement: E? = null
        override fun hasNext(): Boolean = inner.hasNext()
        override fun next(): E {
            val element = inner.next()
            lastElement = element
            lastIndex++
            return element
        }

        override fun remove() {
            inner.remove()
            onRemove.invokeAll(lastElement!!, lastIndex)
            lastIndex--
        }

    }
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> = collection.subList(fromIndex, toIndex)
    override fun get(index: Int): E = collection[index]
    override fun indexOf(element: E): Int = collection.indexOf(element)
    override fun lastIndexOf(element: E): Int = collection.lastIndexOf(element)
    override val size: Int get() = collection.size

    override fun replace(list: List<E>) {
        collection.clear()
        collection.addAll(list)
        onReplace.invokeAll(this)
        onUpdate.invokeAll(this)
    }

    override fun move(fromIndex: Int, toIndex: Int) {
        val item = collection.removeAt(fromIndex)
        collection.add(toIndex, item)
        onMove.invokeAll(item, fromIndex, toIndex)
    }
}

fun <E> observableListOf(vararg items: E) = ObservableListWrapper(items.toMutableList())