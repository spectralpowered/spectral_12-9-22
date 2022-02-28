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
class ObservablePropertyMapped<S, T>(val actualObservable: ObservableProperty<S>, val mapper: (S) -> T, val reverseMapper: (T) -> S) :
    MutableObservableProperty<T> {

    val actionToWrapper = HashMap<(T) -> Unit, Wrapper>()

    inner class Wrapper(val func: (T) -> Unit) : (S) -> Unit {
        override fun invoke(p1: S) {
            func(mapper(p1))
        }
    }

    val notPartOfWrapper by lazy { IllegalArgumentException("Function not a wrapper in this KObservableMapped.") }

    override val size: Int get() = actualObservable.size
    override fun contains(element: (T) -> Unit): Boolean =
            actualObservable.contains(actionToWrapper[element] ?: throw notPartOfWrapper)

    override fun containsAll(elements: Collection<(T) -> Unit>): Boolean = actualObservable.containsAll(
            elements.map { actionToWrapper[it] ?: throw notPartOfWrapper }
    )

    override fun isEmpty(): Boolean = actualObservable.isEmpty()
    override fun clear() {
        actualObservable.clear()
    }

    override fun iterator(): MutableIterator<(T) -> Unit> = actionToWrapper.keys.iterator()
    override fun remove(element: (T) -> Unit): Boolean {
        val wrapper = actionToWrapper[element] ?: throw notPartOfWrapper
        actionToWrapper.remove(wrapper.func)
        return actualObservable.remove(wrapper)
    }

    override fun removeAll(elements: Collection<(T) -> Unit>): Boolean = actualObservable.removeAll(
            elements.map {
                val wrapper = actionToWrapper[it] ?: throw notPartOfWrapper
                actionToWrapper.remove(wrapper.func)
                wrapper
            }
    )

    override fun retainAll(elements: Collection<(T) -> Unit>): Boolean = throw UnsupportedOperationException()

    override fun add(element: (T) -> Unit): Boolean {
        val wrapper = Wrapper(element)
        actionToWrapper[element] = wrapper
        return actualObservable.add(wrapper)
    }

    override fun addAll(elements: Collection<(T) -> Unit>): Boolean {
        val value = mapper(actualObservable.value)

        return actualObservable.addAll(elements.map {
            val wrapper = Wrapper(it)
            actionToWrapper[it] = wrapper
            wrapper
        })
    }

    override var value: T
        get() = mapper(actualObservable.value)
        set(value) {
            if (actualObservable is MutableObservableProperty) {
                actualObservable.value = reverseMapper(value)
            } else {
                throw IllegalAccessException()
            }
        }

    override fun update() {
        actualObservable.update()
    }
}

inline fun <S, T> MutableObservableProperty<S>.mapObservable(noinline mapper: (S) -> T, noinline reverseMapper: (T) -> S): ObservablePropertyMapped<S, T> {
    return ObservablePropertyMapped(this, mapper, reverseMapper)
}

inline fun <S, T> ObservableProperty<S>.mapReadOnly(noinline mapper: (S) -> T): ObservablePropertyMapped<S, T> {
    return ObservablePropertyMapped(this, mapper, { throw IllegalAccessException() })
}

inline fun <T> ObservableProperty<T?>.notNull(default: T): ObservablePropertyMapped<T?, T> {
    return ObservablePropertyMapped(this, { it ?: default }, { it })
}