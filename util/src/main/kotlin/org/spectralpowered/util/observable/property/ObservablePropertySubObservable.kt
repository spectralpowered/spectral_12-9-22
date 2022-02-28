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

/**
 *
 * Created by Kyle Escobar on 2/22/16.
 */
class ObservablePropertySubObservable<A, B>(
    val owningObservable: ObservableProperty<A>,
    val getter: (A) -> ObservableProperty<B>
) : EnablingMutableCollection<(B) -> Unit>(), MutableObservableProperty<B> {

    var currentSub: ObservableProperty<B>? = null

    override var value: B
        get() = owningObservable.value.let(getter).value
        set(value) {
            val currentSub = owningObservable.value.let(getter)
            if (currentSub is MutableObservableProperty<B>) {
                currentSub.value = value
            } else throw IllegalAccessException("ObservableProperty is not mutable")
        }

    val outerCallback = { a: A ->
        update()
        resub()
    }
    val innerCallback = { b: B -> update() }

    override fun enable() {
        owningObservable.add(outerCallback)
        resub()
    }

    override fun disable() {
        owningObservable.remove(outerCallback)
        unsub()
    }

    private fun resub() {
        unsub()
        val sub = owningObservable.value.let(getter)
        sub += innerCallback
        currentSub = sub
    }

    private fun unsub() {
        currentSub?.remove(innerCallback)
        currentSub = null
    }
}

fun <A, B> ObservableProperty<A>.subObs(getterFun: (A) -> ObservableProperty<B>) = ObservablePropertySubObservable(this, getterFun)