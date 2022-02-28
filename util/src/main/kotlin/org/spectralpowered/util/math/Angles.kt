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
 * Various math functions.
 * Created by Kyle Escobar on 9/28/15.
 */

/**
 * Degrees from this angle (in degrees) to another angle (in degrees).
 */
infix fun Float.degreesTo(to: Float): Float {
    return ((to - this + 180).remainder(360f)) - 180
}

/**
 * Radians from this angle (in radians) to another angle (in radians).
 */
infix fun Float.radiansTo(to: Float): Float {
    return (((to - this + Math.PI).remainder(Math.PI * 2)) - Math.PI).toFloat()
}

/**
 * Degrees from this angle (in degrees) to another angle (in degrees).
 */
infix fun Double.degreesTo(to: Double): Double {
    return ((to - this + 180).remainder(360.0)) - 180
}

/**
 * Radians from this angle (in radians) to another angle (in radians).
 */
infix fun Double.radiansTo(to: Double): Double {
    return ((to - this + Math.PI).remainder(Math.PI * 2)) - Math.PI
}