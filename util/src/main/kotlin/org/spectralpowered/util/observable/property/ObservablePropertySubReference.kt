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
import kotlin.reflect.KMutableProperty1

class ObservablePropertySubReference<A, B>(
    val observable: ObservableProperty<A>,
    val getterFun: (A) -> B,
    val setterFun: (A, B) -> Unit
) : EnablingMutableCollection<(B) -> Unit>(), MutableObservableProperty<B> {
    override var value: B
        get() = getterFun(observable.value)
        set(value) {
            setterFun(observable.value, value)
            observable.update()
        }

    val callback = { a: A -> update() }
    override fun enable() {
        observable.add(callback)
    }

    override fun disable() {
        observable.remove(callback)
    }


    override fun spliterator(): Spliterator<(B) -> Unit> {
        return super<EnablingMutableCollection>.spliterator()
    }

    override fun removeIf(filter: Predicate<in (B) -> Unit>): Boolean {
        return super<EnablingMutableCollection>.removeIf(filter)
    }

    override fun forEach(action: Consumer<in (B) -> Unit>) {
        super<EnablingMutableCollection>.forEach(action)
    }
}

fun <A, B> ObservableProperty<A>.sub(getterFun: (A) -> B)
        = ObservablePropertySubReference(this, getterFun, { a, b -> throw IllegalAccessException("This is read only.") })

fun <A, B> ObservableProperty<A>.sub(getterFun: (A) -> B, setterFun: (A, B) -> Unit)
        = ObservablePropertySubReference(this, getterFun, setterFun)

fun <A, B> ObservableProperty<A>.sub(property: KMutableProperty1<A, B>)
        = ObservablePropertySubReference(this, { property.get(it) }, { a, b -> property.set(a, b) })