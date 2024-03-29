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

package org.spectralpowered.natives.offset

import it.unimi.dsi.fastutil.bytes.ByteArrayList
import org.spectralpowered.natives.memory.Module

class Pattern(
    val module: Module,
    val patternOffset: Long,
    val addressOffset: Long,
    val read: Boolean,
    val subtract: Boolean
) {

    operator fun invoke(vararg pattern: Any): Offset {
        val bytes = ByteArrayList()

        for(mask in pattern) when(mask) {
            is Number -> bytes.add(mask.toByte())
            is RepeatedInt -> repeat(mask.repeats) { bytes.add(mask.value.toByte()) }
        }

        return Offset(
            module,
            patternOffset,
            addressOffset,
            read,
            subtract,
            bytes.toByteArray()
        )
    }
}