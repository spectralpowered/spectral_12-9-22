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

package org.spectralpowered.util.lifecycle

/**
 * Extensions to attach things to lifecycles.
 * Created by Kyle Escobar on 6/1/16.
 */

fun <T> LifecycleConnectable.listen(collection: MutableCollection<T>, item: T) {
    connect(object : LifecycleListener {
        override fun onStart() {
            collection.add(item)
        }

        override fun onStop() {
            collection.remove(item)
        }
    })
}

fun <A, B> LifecycleConnectable.listen(eventA: MutableCollection<(A) -> Unit>, eventB: MutableCollection<(B) -> Unit>, listener: () -> Unit) {
    connect(object : LifecycleListener {
        val listenerA = { it: A -> listener() }
        val listenerB = { it: B -> listener() }

        override fun onStart() {
            eventA.add(listenerA)
            eventB.add(listenerB)
        }

        override fun onStop() {
            eventA.remove(listenerA)
            eventB.remove(listenerB)
        }
    })
}

fun <A, B, C> LifecycleConnectable.listen(
        eventA: MutableCollection<(A) -> Unit>,
        eventB: MutableCollection<(B) -> Unit>,
        eventC: MutableCollection<(C) -> Unit>,
        listener: () -> Unit
) {
    connect(object : LifecycleListener {
        val listenerA = { it: A -> listener() }
        val listenerB = { it: B -> listener() }
        val listenerC = { it: C -> listener() }

        override fun onStart() {
            eventA.add(listenerA)
            eventB.add(listenerB)
            eventC.add(listenerC)
        }

        override fun onStop() {
            eventA.remove(listenerA)
            eventB.remove(listenerB)
            eventC.remove(listenerC)
        }
    })
}

fun <T> LifecycleConnectable.bind(event: MutableCollection<(T) -> Unit>, firstRunValue: T, listener: (T) -> Unit) {
    connect(object : LifecycleListener {
        override fun onStart() {
            event.add(listener)
            listener(firstRunValue)
        }

        override fun onStop() {
            event.remove(listener)
        }
    })
}

fun LifecycleConnectable.bind(event: MutableCollection<() -> Unit>, listener: () -> Unit) {
    connect(object : LifecycleListener {
        override fun onStart() {
            event.add(listener)
            listener()
        }

        override fun onStop() {
            event.remove(listener)
        }
    })
}