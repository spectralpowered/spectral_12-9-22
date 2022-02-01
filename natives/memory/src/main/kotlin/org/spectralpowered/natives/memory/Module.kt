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

package org.spectralpowered.natives.memory

import com.sun.jna.Memory
import com.sun.jna.Pointer

interface Module : Source, Addressed {

    val process: Process

    val name: String

    val size: Long

    override fun read(address: Pointer, data: Pointer, length: Int)
        = process.read(address, data, length)

    override fun read(address: Long, data: Pointer, length: Int)
        = process.read(address, data, length)

    override fun write(address: Pointer, data: Pointer, length: Int)
        = process.write(address, data, length)

    override fun write(address: Long, data: Pointer, length: Int)
        = process.write(address, data, length)
}