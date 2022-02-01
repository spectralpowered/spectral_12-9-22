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

package org.spectralpowered.natives.memory.ext

import org.spectralpowered.natives.memory.Source

inline operator fun <reified T : Number> Source.get(address: Long, offset: Long = 0L): T {
    return when(T::class) {
        Byte::class -> read(address, 1)?.getByte(offset)
        Short::class -> read(address, 2)?.getShort(offset)
        Char::class -> read(address, 2)?.getChar(offset)
        Int::class -> read(address, 4)?.getInt(offset)
        Float::class -> read(address, 4)?.getFloat(offset)
        Double::class -> read(address, 8)?.getDouble(offset)
        Long::class -> read(address, 8)?.getLong(offset)
        else -> throw IllegalAccessError("Failed to read memory at address: 0x${address.toString(16)}. Invalid primitive type.")
    } as T? ?: 0 as T
}

inline operator fun <reified T : Number> Source.set(address: Long, value: T) {
    when(T::class) {
        Byte::class -> write(address, 1) { setByte(0, value.toByte()) }
        Short::class -> write(address, 2) { setShort(0, value.toShort()) }
        Char::class -> write(address, 2) { setChar(0, value.toChar()) }
        Int::class -> write(address, 4) { setInt(0, value.toInt()) }
        Float::class -> write(address, 4) { setFloat(0, value.toFloat()) }
        Double::class -> write(address, 8) { setDouble(0, value.toDouble()) }
        Long::class -> write(address, 8) { setLong(0, value.toLong()) }
        else -> throw IllegalAccessError("Failed to write memory at address 0x${address.toString(16)}. Invalid primitive type.")
    }
}