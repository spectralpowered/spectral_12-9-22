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
 * Created by Kyle Escobar on 12/14/16.
 */


class CollectionWriteOnlyMapping<S, E>(val source: MutableCollection<S>, val inputMapper: (E) -> S) : MutableCollection<E> {
    val map = HashMap<E, S>()

    override val size: Int get() = source.size

    override fun add(element: E): Boolean {
        val mapped = inputMapper(element)
        map[element] = mapped
        return source.add(mapped)
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val mapped = elements.map(inputMapper)
        map.putAll(elements.zip(mapped))
        return source.addAll(mapped)
    }

    override fun remove(element: E): Boolean = source.remove(map.remove(element))
    override fun removeAll(elements: Collection<E>): Boolean = source.removeAll(elements.map { map[it] })
    override fun retainAll(elements: Collection<E>): Boolean = source.retainAll(elements.map { map[it] })

    override fun contains(element: E): Boolean = throw IllegalAccessException()
    override fun containsAll(elements: Collection<E>): Boolean = throw IllegalAccessException()
    override fun isEmpty(): Boolean = throw IllegalAccessException()
    override fun clear() = throw IllegalAccessException()
    override fun iterator(): MutableIterator<E> = throw IllegalAccessException()
}

fun <S, E> MutableCollection<S>.mappingWriteOnly(inputMapper: (E) -> S) = CollectionWriteOnlyMapping(this, inputMapper)