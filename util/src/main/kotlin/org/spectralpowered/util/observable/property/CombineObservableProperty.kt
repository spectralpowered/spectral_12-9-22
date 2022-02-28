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
 *
 * Created by Kyle Escobar on 12/2/16.
 */
class CombineObservableProperty2<A, B, T>(
    val observableA: ObservableProperty<A>,
    val observableB: ObservableProperty<B>,
    val combine: (A, B) -> T
) : EnablingMutableCollection<(T) -> Unit>(), ObservableProperty<T> {

    override var value = combine(observableA.value, observableB.value)

    override fun update() {
        value = combine(observableA.value, observableB.value)
        super.update()
    }

    val callbackA = { item: A ->
        update()
    }
    val callbackB = { item: B ->
        update()
    }

    override fun enable() {
        observableA.add(callbackA)
        observableB.add(callbackB)
        update()
    }

    override fun disable() {
        observableA.remove(callbackA)
        observableB.remove(callbackB)
    }
}

class CombineObservableProperty3<A, B, C, T>(
    val observableA: ObservableProperty<A>,
    val observableB: ObservableProperty<B>,
    val observableC: ObservableProperty<C>,
    val combine: (A, B, C) -> T
) : EnablingMutableCollection<(T) -> Unit>(), ObservableProperty<T> {

    override var value = combine(observableA.value, observableB.value, observableC.value)

    override fun update() {
        value = combine(observableA.value, observableB.value, observableC.value)
        super.update()
    }

    val callbackA = { item: A ->
        update()
    }
    val callbackB = { item: B ->
        update()
    }
    val callbackC = { item: C ->
        update()
    }

    override fun enable() {
        observableA.add(callbackA)
        observableB.add(callbackB)
        observableC.add(callbackC)
    }

    override fun disable() {
        observableA.remove(callbackA)
        observableB.remove(callbackB)
        observableC.remove(callbackC)
    }
}

@Suppress("UNCHECKED_CAST")
class CombineObservablePropertyBlind<T>(
    val observables: Collection<ObservableProperty<*>>,
    val combine: () -> T
) : EnablingMutableCollection<(T) -> Unit>(), ObservableProperty<T> {

    constructor(vararg observables: ObservableProperty<*>, combine: () -> T) : this(observables.toList(), combine)

    override var value = combine()

    override fun update() {
        value = combine()
        super.update()
    }

    val callbacks = HashMap<ObservableProperty<Any?>, (Any?) -> Unit>()

    override fun enable() {
        callbacks += observables.map {
            val newListener = { _: Any? -> update() }
            val itFake = it as ObservableProperty<Any?>
            itFake.add(newListener)
            itFake to newListener
        }
    }

    override fun disable() {
        callbacks.forEach { (key, value) -> key.remove(value) }
        callbacks.clear()
    }
}