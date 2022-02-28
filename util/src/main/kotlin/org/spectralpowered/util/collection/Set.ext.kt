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

/**
 * Created by Kyle Escobar on 5/6/16.
 */
class MappedMutableSet<S, E>(val source: MutableSet<S>, val mapper: (S) -> E, val reverseMapper: (E) -> S) : MutableSet<E> {
    override val size: Int get() = source.size
    override fun add(element: E): Boolean = source.add(reverseMapper(element))
    override fun addAll(elements: Collection<E>): Boolean = source.addAll(elements.map(reverseMapper))
    override fun clear() = source.clear()
    override fun iterator(): MutableIterator<E> = source.iterator().mapping(mapper, reverseMapper)
    override fun remove(element: E): Boolean = source.remove(reverseMapper(element))
    override fun removeAll(elements: Collection<E>): Boolean = source.removeAll(elements.map(reverseMapper))
    override fun retainAll(elements: Collection<E>): Boolean = source.retainAll(elements.map(reverseMapper))
    override fun contains(element: E): Boolean = source.contains(reverseMapper(element))
    override fun containsAll(elements: Collection<E>): Boolean = source.containsAll(elements.map(reverseMapper))
    override fun isEmpty(): Boolean = source.isEmpty()
}

fun <S, E> MutableSet<S>.mapping(mapper: (S) -> E, reverseMapper: (E) -> S): MutableSet<E> = MappedMutableSet(this, mapper, reverseMapper)
fun <S, E> MutableSet<S>.mapping(reverseMapper: (E) -> S): MutableSet<E> = MappedMutableSet(this, { throw IllegalAccessException() }, reverseMapper)