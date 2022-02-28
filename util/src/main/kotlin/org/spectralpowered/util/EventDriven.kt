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

package org.spectralpowered.util

/**
 * Various functions for event-driven programming.
 * Created by Kyle Escobar on 5/31/2016.
 */

@Deprecated("Use invokeAll instead.", ReplaceWith("invokeAll()")) inline fun Iterable<() -> Unit>.runAll() = invokeAll()

inline fun Iterable<() -> Unit>.invokeAll() {
    for (item in this) {
        item()
    }
}

@Deprecated("Use invokeAll instead.", ReplaceWith("invokeAll(a)")) inline fun <A> Iterable<(A) -> Unit>.runAll(a: A) = invokeAll(a)
inline fun <A> Iterable<(A) -> Unit>.invokeAll(a: A) {
    for (item in this) {
        item(a)
    }
}

@Deprecated("Use invokeAll instead.", ReplaceWith("invokeAll(a, b)")) inline fun <A, B> Iterable<(A, B) -> Unit>.runAll(a: A, b: B) = invokeAll(a, b)
inline fun <A, B> Iterable<(A, B) -> Unit>.invokeAll(a: A, b: B) {
    for (item in this) {
        item(a, b)
    }
}

@Deprecated("Use invokeAll instead.", ReplaceWith("invokeAll(a, b, c)")) inline fun <A, B, C> Iterable<(A, B, C) -> Unit>.runAll(a: A, b: B, c: C) = invokeAll(a, b, c)
inline fun <A, B, C> Iterable<(A, B, C) -> Unit>.invokeAll(a: A, b: B, c: C) {
    for (item in this) {
        item(a, b, c)
    }
}

@Deprecated("Use invokeAll instead.", ReplaceWith("invokeAll(a, b, c, d)")) inline fun <A, B, C, D> Iterable<(A, B, C, D) -> Unit>.runAll(a: A, b: B, c: C, d: D) = invokeAll(a, b, c, d)
inline fun <A, B, C, D> Iterable<(A, B, C, D) -> Unit>.invokeAll(a: A, b: B, c: C, d: D) {
    for (item in this) {
        item(a, b, c, d)
    }
}