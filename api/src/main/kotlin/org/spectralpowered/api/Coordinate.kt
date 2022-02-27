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

package org.spectralpowered.api

sealed class Coordinate {

    abstract val x: Int
    abstract val y: Int

    data class Local(
        override val x: Int,
        override val y: Int
    ) : Coordinate() {

    }


    data class Scene(
        override val x: Int,
        override val y: Int
    ) : Coordinate() {

    }

   data class World(
        override val x: Int,
        override val y: Int
    ) : Coordinate() {

    }
}