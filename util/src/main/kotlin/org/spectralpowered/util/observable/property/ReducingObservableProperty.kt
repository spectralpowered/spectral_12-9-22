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
import java.util.function.Consumer
import java.util.function.Predicate

/**
 * Created by Kyle Escobar on 12/2/16.
 */
class ReducingObservableProperty<E, T>(
    val observables: List<ObservableProperty<E>>,
    val reduce: (List<E>) -> T
) : EnablingMutableCollection<(T) -> Unit>(), ObservableProperty<T> {

    override var value = reduce(observables.map { it.value })
    override fun update() {
        value = reduce(observables.map { it.value })
        super.update()
    }

    val callback = { item: E ->
        update()
    }

    override fun enable() {
        for (observable in observables) {
            observable.add(callback)
        }
    }

    override fun disable() {
        for (observable in observables) {
            observable.remove(callback)
        }
    }

    override fun spliterator(): Spliterator<(T) -> Unit> {
        return super<EnablingMutableCollection>.spliterator()
    }

    override fun removeIf(filter: Predicate<in (T) -> Unit>): Boolean {
        return super<EnablingMutableCollection>.removeIf(filter)
    }

    override fun forEach(action: Consumer<in (T) -> Unit>) {
        super<EnablingMutableCollection>.forEach(action)
    }
}

fun <T, E> List<ObservableProperty<E>>.reducing(reduce: (List<E>) -> T): ObservableProperty<T> = ReducingObservableProperty(this, reduce)