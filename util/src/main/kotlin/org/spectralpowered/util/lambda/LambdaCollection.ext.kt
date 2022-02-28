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

package org.spectralpowered.util.lambda

/**
 *
 * Created by Kyle Escobar on 9/27/17.
 */

inline fun <A> MutableCollection<(A) -> Unit>.addInvokeOnce(crossinline lambda: (A) -> Unit) {
    add(object : (A) -> Unit {
        override fun invoke(a: A) {
            lambda.invoke(a)
            remove(this)
        }
    })
}

inline fun <A, B> MutableCollection<(A, B) -> Unit>.addInvokeOnce(crossinline lambda: (A, B) -> Unit) {
    add(object : (A, B) -> Unit {
        override fun invoke(a: A, b: B) {
            lambda.invoke(a, b)
            remove(this)
        }
    })
}

inline fun <A, B, C> MutableCollection<(A, B, C) -> Unit>.addInvokeOnce(crossinline lambda: (A, B, C) -> Unit) {
    add(object : (A, B, C) -> Unit {
        override fun invoke(a: A, b: B, c: C) {
            lambda.invoke(a, b, c)
            remove(this)
        }
    })
}

inline fun <A> MutableCollection<(A) -> Unit>.addInvokeOnce(crossinline filter: (A) -> Boolean, crossinline lambda: (A) -> Unit) {
    add(object : (A) -> Unit {
        override fun invoke(a: A) {
            if (filter.invoke(a)) {
                lambda.invoke(a)
                remove(this)
            }
        }
    })
}

inline fun <A, B> MutableCollection<(A, B) -> Unit>.addInvokeOnce(crossinline filter: (A, B) -> Boolean, crossinline lambda: (A, B) -> Unit) {
    add(object : (A, B) -> Unit {
        override fun invoke(a: A, b: B) {
            if (filter.invoke(a, b)) {
                lambda.invoke(a, b)
                remove(this)
            }
        }
    })
}

inline fun <A, B, C> MutableCollection<(A, B, C) -> Unit>.addInvokeOnce(crossinline filter: (A, B, C) -> Boolean, crossinline lambda: (A, B, C) -> Unit) {
    add(object : (A, B, C) -> Unit {
        override fun invoke(a: A, b: B, c: C) {
            if (filter.invoke(a, b, c)) {
                lambda.invoke(a, b, c)
                remove(this)
            }
        }
    })
}