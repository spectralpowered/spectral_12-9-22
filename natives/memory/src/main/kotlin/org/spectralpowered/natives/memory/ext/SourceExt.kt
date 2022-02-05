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

import com.sun.jna.Pointer
import org.spectralpowered.natives.memory.MemoryCache
import org.spectralpowered.natives.memory.Source

/**
 * Writes to the source at the specified address using a supplied memory.
 *
 * @param address The native address to write to.
 * @param bytes The amount of bytes for the supplied memory.
 * @param writeBody Applies the write to the data.
 */
inline fun Source.write(address: Long, bytes: Int, writeBody: Pointer.() -> Unit) {
    val resource = MemoryCache[bytes]
    resource.writeBody()
    write(address, resource)
}

/**
 * Writes to the source at the specified address using a supplied memory.
 *
 * @param address The native address to write to.
 * @param bytes The amount of bytes for the supplied memory.
 * @param writeBody Applies the write to the data.
 */
inline fun Source.write(address: Int, bytes: Int, writeBody: Pointer.() -> Unit)
        = write(address.toLong(), bytes, writeBody)

/**
 * Reads from the source at the specified address with an implicit return type.
 *
 * @param address The native address to read from.
 * @param offset The offset in bytes off the native address.
 * @param T The implicit return type of one of the following:
 *                 * Byte
 *                 * Short
 *                 * Char
 *                 * Int
 *                 * Long
 *                 * Float
 *                 * Double
 *                 * Boolean
 */
inline operator fun <reified T : Any> Source.get(address: Long, offset: Long = 0) = when (T::class.java) {
    java.lang.Byte::class.java -> byte(address, offset)
    java.lang.Short::class.java -> short(address, offset)
    java.lang.Character::class.java -> char(address, offset)
    java.lang.Integer::class.java -> int(address, offset)
    java.lang.Long::class.java -> long(address, offset)
    java.lang.Float::class.java -> float(address, offset)
    java.lang.Double::class.java -> double(address, offset)
    java.lang.Boolean::class.java -> boolean(address, offset)
    else -> throw IllegalArgumentException()
} as T

/**
 * Reads from the source at the specified address with an implicit return type.
 *
 * @param address The native address to read from.
 * @param offset The offset in bytes off the native address.
 * @param T The implicit return type of one of the following:
 *                 * Byte
 *                 * Short
 *                 * Char
 *                 * Int
 *                 * Long
 *                 * Float
 *                 * Double
 *                 * Boolean
 */
inline operator fun <reified T : Any> Source.get(address: Int, offset: Long = 0): T
        = get(address.toLong(), offset)

/**
 * Sets the source at the specified address with an offset's value.
 *
 * @receiver Source
 * @param address Long
 * @param offset Long
 * @param value T
 */
inline operator fun <reified T : Any> Source.set(address: Long = this.address, offset: Long = 0L, value: T) {
    when(T::class) {
        Byte::class -> this[address + offset] = value as Byte
        Short::class -> this[address + offset] = value as Short
        Char::class -> this[address + offset] = value as Char
        Int::class -> this[address + offset] = value as Int
        Float::class -> this[address + offset] = value as Float
        Double::class -> this[address + offset] = value as Double
        Long::class -> this[address + offset] = value as Long
        else -> throw IllegalArgumentException("Failed to set value of type: ${T::class.simpleName} to memory.")
    }
}

/**
 * Reads an unsigned integer from and address + and optional offset.
 *
 * @receiver Source
 * @param address Long
 * @param offset Long
 * @return Long
 */
fun Source.uint(address: Long, offset: Long = 0L) = int(address, offset).unsign()
