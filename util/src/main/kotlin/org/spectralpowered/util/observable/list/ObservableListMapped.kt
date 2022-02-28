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

import org.spectralpowered.util.collection.mapping
import org.spectralpowered.util.collection.mappingWriteOnly
import org.spectralpowered.util.observable.property.mapReadOnly

/**
 * Created by Kyle Escobar on 5/6/16.
 */
class ObservableListMapped<S, E>(val source: ObservableList<S>, val mapper: (S) -> E, val reverseMapper: (E) -> S) :
    ObservableList<E> {
    override val size: Int get() = source.size

    override fun contains(element: E): Boolean = source.contains(reverseMapper(element))
    override fun containsAll(elements: Collection<E>): Boolean = source.containsAll(elements.map(reverseMapper))
    override fun get(index: Int): E = mapper(source.get(index))
    override fun indexOf(element: E): Int = source.indexOf(reverseMapper(element))
    override fun isEmpty(): Boolean = source.isEmpty()
    override fun lastIndexOf(element: E): Int = source.lastIndexOf(reverseMapper(element))
    override fun add(element: E): Boolean = source.add(reverseMapper(element))
    override fun add(index: Int, element: E) = source.add(index, reverseMapper(element))
    override fun move(fromIndex: Int, toIndex: Int) = source.move(fromIndex, toIndex)
    override fun addAll(index: Int, elements: Collection<E>): Boolean = source.addAll(index, elements.map(reverseMapper))
    override fun addAll(elements: Collection<E>): Boolean = source.addAll(elements.map(reverseMapper))
    override fun clear() = source.clear()
    override fun remove(element: E): Boolean = source.remove(reverseMapper(element))
    override fun removeAll(elements: Collection<E>): Boolean = source.removeAll(elements.map(reverseMapper))
    override fun removeAt(index: Int): E = mapper(source.removeAt(index))
    override fun retainAll(elements: Collection<E>): Boolean = source.retainAll(elements.map(reverseMapper))
    override fun set(index: Int, element: E): E = mapper(source.set(index, reverseMapper(element)))
    override fun subList(fromIndex: Int, toIndex: Int): MutableList<E> = source.subList(fromIndex, toIndex).map(mapper).toMutableList()

    override fun listIterator(): MutableListIterator<E> = source.listIterator().mapping(mapper, reverseMapper)
    override fun listIterator(index: Int): MutableListIterator<E> = source.listIterator(index).mapping(mapper, reverseMapper)
    override fun iterator(): MutableIterator<E> = source.iterator().mapping(mapper, reverseMapper)
    override fun replace(list: List<E>) = source.replace(list.map(reverseMapper))

    val listenerMapper = { input: (E, Int) -> Unit ->
        { element: S, index: Int ->
            input(mapper(element), index)
        }
    }
    override val onAdd: MutableCollection<(E, Int) -> Unit> = source.onAdd.mappingWriteOnly(listenerMapper)
    override val onRemove: MutableCollection<(E, Int) -> Unit> = source.onRemove.mappingWriteOnly(listenerMapper)
    override val onMove: MutableCollection<(E, Int, Int) -> Unit> = source.onMove.mappingWriteOnly { input: (E, Int, Int) -> Unit ->
        { element: S, oldIndex: Int, index: Int ->
            input(mapper(element), oldIndex, index)
        }
    }
    override val onChange: MutableCollection<(E, E, Int) -> Unit> = source.onChange.mappingWriteOnly { input: (E, E, Int) -> Unit ->
        { old: S, element: S, index: Int ->
            input(mapper(old), mapper(element), index)
        }
    }

    override val onUpdate = source.onUpdate.mapReadOnly<ObservableList<S>, ObservableList<E>>({ it -> this@ObservableListMapped })
    override val onReplace: MutableCollection<(ObservableList<E>) -> Unit> = source.onReplace.mappingWriteOnly({ input -> { input(this) } })
}

@Deprecated("This has been renamed to 'mapping'.", ReplaceWith("mapping(mapper, reverseMapper)", "org.spectralpowered.util.mapping"))
fun <S, E> ObservableList<S>.mapObservableList(mapper: (S) -> E, reverseMapper: (E) -> S): ObservableListMapped<S, E> = ObservableListMapped(this, mapper, reverseMapper)

@Deprecated("This has been renamed to 'mapping'.", ReplaceWith("mapping(mapper)", "org.spectralpowered.util.mapping"))
fun <S, E> ObservableList<S>.mapObservableList(mapper: (S) -> E): ObservableListMapped<S, E> = ObservableListMapped(this, mapper, { throw IllegalArgumentException() })

fun <S, E> ObservableList<S>.mapping(mapper: (S) -> E, reverseMapper: (E) -> S): ObservableListMapped<S, E> = ObservableListMapped(this, mapper, reverseMapper)
fun <S, E> ObservableList<S>.mapping(mapper: (S) -> E): ObservableListMapped<S, E> = ObservableListMapped(this, mapper, { throw IllegalArgumentException() })