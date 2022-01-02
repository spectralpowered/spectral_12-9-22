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

package org.spectralpowered.natives.jvm.util

import org.jire.arrowhead.Process
import org.jire.arrowhead.get
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

inline fun <reified T : Any> Process.pointer(address: Long, vararg offsets: Long): ReadWriteProperty<Any, T> {
    var current = address
    for(i in offsets.indices) {
        current = this.long(current) + offsets[i]
    }
    return this.invoke(current)
}

inline operator fun <reified T : Any> Process.invoke(address: Long, offset: Long = 0L) = object : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return this@invoke.get(address + offset) as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = when(T::class) {
        Byte::class -> this@invoke.set(address + offset, value as Byte)
        Short::class -> this@invoke.set(address + offset, value as Short)
        Int::class -> this@invoke.set(address + offset, value as Int)
        Double::class -> this@invoke.set(address + offset, value as Double)
        Float::class -> this@invoke.set(address + offset, value as Float)
        Long::class -> this@invoke.set(address + offset, value as Long)
        Char::class -> this@invoke.set(address + offset, value as Char)
        Boolean::class -> this@invoke.set(address + offset, value as Boolean)
        else -> throw IllegalArgumentException()
    }
}