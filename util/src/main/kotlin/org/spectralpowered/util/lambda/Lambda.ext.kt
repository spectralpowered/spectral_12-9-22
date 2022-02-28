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
 * Created by Kyle Escobar on 11/14/16.
 */
@Deprecated("Use 'transform' instead.", ReplaceWith("transform(mapper)"))
inline fun <A, B> (() -> A).map(noinline mapper: (A) -> B): () -> B = transform(mapper)
inline fun <A, B> (() -> A).transform(noinline mapper: (A) -> B): () -> B {
    return { this.invoke().let(mapper) }
}