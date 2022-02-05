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

package org.spectralpowered.engine.util

import org.spectralpowered.engine.Engine
import org.spectralpowered.natives.memory.Addressed
import org.spectralpowered.natives.memory.ext.get
import org.spectralpowered.natives.memory.ext.set
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

operator fun Long.get(vararg offsets: Long): Long {
    var value: Long = Engine.module[this]
    for(i in 1 until offsets.size) {
        value = Engine.module[value]
    }
    return value
}

operator fun Addressed.get(vararg offsets: Long): Addressed {
    var value: Long = Engine.module[this.address]
    for(i in 1 until offsets.size) {
        value = Engine.module[value]
    }
    return object : Addressed { override val address: Long = value }
}

inline fun <reified T : Any> Addressed.field(offset: Long = 0L) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T
        = Engine.module[this@field.address + offset]

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        Engine.module[this@field.address + offset] = value
    }
}

inline fun <reified T : Any> Addressed.global(address: Long, offset: Long = 0L) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T
            = Engine.module[address + offset]

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        Engine.module[address + offset] = value
    }
}