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

package org.spectralpowered.util.collection

import java.util.*

/**
 * Created by Kyle Escobar on 6/14/16.
 */
class MappedList<S, T>(val around: List<S>, val mapper: (S) -> T, val reverseMapper: (T) -> S) : List<T> {
    override fun contains(element: T): Boolean = around.contains(reverseMapper(element))
    override fun containsAll(elements: Collection<T>): Boolean = around.containsAll(elements.map(reverseMapper))
    override fun get(index: Int): T = mapper(around.get(index))
    override fun indexOf(element: T): Int = around.indexOf(reverseMapper(element))
    override fun isEmpty(): Boolean = around.isEmpty()
    override fun iterator(): Iterator<T> = around.iterator().mapping(mapper)
    override fun lastIndexOf(element: T): Int = around.lastIndexOf(reverseMapper(element))
    override fun listIterator(): ListIterator<T> = around.listIterator().mapping(mapper)
    override fun listIterator(index: Int): ListIterator<T> = around.listIterator(index).mapping(mapper)
    override fun subList(fromIndex: Int, toIndex: Int): List<T> = around.subList(fromIndex, toIndex).mapping(mapper, reverseMapper)
    override val size: Int get() = around.size

}

fun <S, T> List<S>.mapping(mapper: (S) -> T, reverseMapper: (T) -> S): List<T> = MappedList(this, mapper, reverseMapper)

class MappedMutableList<S, T>(val around: MutableList<S>, val mapper: (S) -> T, val reverseMapper: (T) -> S) : MutableList<T> {
    override fun add(element: T): Boolean = around.add(reverseMapper(element))
    override fun add(index: Int, element: T) = around.add(index, reverseMapper(element))
    override fun addAll(index: Int, elements: Collection<T>): Boolean = around.addAll(index, elements.map(reverseMapper))
    override fun addAll(elements: Collection<T>): Boolean = around.addAll(elements.map(reverseMapper))
    override fun clear() = around.clear()
    override fun remove(element: T): Boolean = around.remove(reverseMapper(element))
    override fun removeAll(elements: Collection<T>): Boolean = around.removeAll(elements.map(reverseMapper))
    override fun removeAt(index: Int): T = mapper(around.removeAt(index))
    override fun retainAll(elements: Collection<T>): Boolean = around.retainAll(elements.map(reverseMapper))
    override fun set(index: Int, element: T): T {
        around[index] = reverseMapper(element)
        return element
    }

    override fun contains(element: T): Boolean = around.contains(reverseMapper(element))
    override fun containsAll(elements: Collection<T>): Boolean = around.containsAll(elements.map(reverseMapper))
    override fun get(index: Int): T = mapper(around.get(index))
    override fun indexOf(element: T): Int = around.indexOf(reverseMapper(element))
    override fun isEmpty(): Boolean = around.isEmpty()
    override fun iterator(): MutableIterator<T> = around.iterator().mapping(mapper, reverseMapper)
    override fun lastIndexOf(element: T): Int = around.lastIndexOf(reverseMapper(element))
    override fun listIterator(): MutableListIterator<T> = around.listIterator().mapping(mapper, reverseMapper)
    override fun listIterator(index: Int): MutableListIterator<T> = around.listIterator(index).mapping(mapper, reverseMapper)
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = around.subList(fromIndex, toIndex).mappingMutable(mapper, reverseMapper)
    override val size: Int get() = around.size

}

fun <S, T> MutableList<S>.mappingMutable(mapper: (S) -> T, reverseMapper: (T) -> S): MutableList<T> = MappedMutableList(this, mapper, reverseMapper)
inline fun <S, T> MutableList<S>.mappingMutableHash(crossinline mapper: (S) -> T): MutableList<T> {
    val forward = WeakHashMap<S, T>()
    val reverse = WeakHashMap<T, S>()
    return MappedMutableList<S, T>(
            this,
            mapper = {
                val current = forward[it]
                if (current != null) return@MappedMutableList current
                val mapped = mapper(it)
                forward.put(it, mapped)
                reverse.put(mapped, it)
                mapped
            },
            reverseMapper = {
                reverse[it]!!
            }
    )
}