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
 * Various extensions functions for lists.
 * Created by Kyle Escobar on 4/26/16.
 */

inline fun <E> List<E>.random(): E {
    return this[Math.random().times(size).toInt()]
}
fun <E> SetupList(setup: (E) -> Unit): MutableList<E> {
    return object : ArrayList<E>() {
        override fun addAll(index: Int, elements: Collection<E>): Boolean {
            elements.forEach(setup)
            return super.addAll(index, elements)
        }

        override fun addAll(elements: Collection<E>): Boolean {
            elements.forEach(setup)
            return super.addAll(elements)
        }

        override fun add(element: E): Boolean {
            setup(element)
            return super.add(element)
        }

        override fun add(index: Int, element: E) {
            setup(element)
            super.add(index, element)
        }

        override fun set(index: Int, element: E): E {
            setup(element)
            return super.set(index, element)
        }
    }
}

fun <E> MutableList<E>.addSorted(item: E, compare: (E, E) -> Boolean): Int {
    var index = 0
    for (it in this) {
        if (compare(item, it)) {
            break
        }
        index++
    }
    add(index, item)
    return index
}

fun <E : Comparable<E>> MutableList<E>.addSorted(item: E): Int {
    var index = 0
    for (it in this) {
        if (item.compareTo(it) < 0) {
            break
        }
        index++
    }
    add(index, item)
    return index
}

fun <E : Comparable<E>> MutableList<E>.addAllSorted(items: Collection<E>) {
    for (item in items) {
        addSorted(item)
    }
}

inline fun <E : Comparable<E>> MutableList<E>.addSortedReverse(item: E): Int {
    var index = 0
    for (it in this) {
        if (item.compareTo(it) > 0) {
            break
        }
        index++
    }
    add(index, item)
    return index
}