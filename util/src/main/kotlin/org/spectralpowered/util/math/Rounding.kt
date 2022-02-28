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

package org.spectralpowered.util.math

/**
 * Various rounding functions
 * Created by Kyle Escobar on 8/10/16.
 */

inline fun Float.round() = Math.round(this)

inline fun Double.round() = Math.round(this)
inline fun Double.roundTo(digit: Int): Double {
    val multiplier = 10.0.pow(-digit.toDouble())
    return this.times(multiplier).round().toDouble().div(multiplier)
}

inline fun Float.roundTo(digit: Int): Float {
    val multiplier = 10.0.pow(-digit.toDouble())
    return this.times(multiplier).round().toDouble().div(multiplier).toFloat()
}

fun main(vararg args: String) {
    println(23.5.roundTo(-1))
}