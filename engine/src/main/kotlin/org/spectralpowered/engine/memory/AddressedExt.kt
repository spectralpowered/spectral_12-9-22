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

package org.spectralpowered.engine.memory

import org.spectralpowered.engine.Engine
import org.spectralpowered.natives.memory.Addressed
import org.spectralpowered.natives.memory.ext.get
import org.spectralpowered.natives.memory.ext.set
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun Long.toAddress(): Addressed = object : Addressed {
    override val address = this@toAddress
}

operator fun Addressed.get(offset: Long) = this.deref()
    .offset(offset)
    .toAddress()
    .address

fun Addressed.deref() =
    if(address < Engine.process.address) {
        Engine.module.long(this.address).toAddress()
    } else {
        Engine.process.long(this.address).toAddress()
    }

fun Addressed.ptr(vararg offsets: Long): Addressed {
    if (offsets.isEmpty()) return this.deref()
    var value = Engine.process.long(this.address + offsets[0])
    for (i in 1 until offsets.size) {
        value = Engine.process.long(value + offsets[i])
    }
    return value.toAddress()
}

inline fun <reified T : Any> Addressed.field(offset: Long) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return Engine.process[this@field[offset]]
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
       Engine.process[this@field[offset]] = value
    }
}

inline fun <reified T : Any> global(offset: Long) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return Engine.module[offset]
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        Engine.module[offset] = value
    }
}

inline fun <reified T : Any> function(offset: Long) =
    ReadOnlyProperty<Any, GlobalFunction<T>> { _, _ ->
        GlobalFunction(Engine.module.address + offset, T::class)
    }

inline fun <reified T : Any> Addressed.vfunction(offset: Long) =
    ReadOnlyProperty<Any, VirtualFunction<T>> { _, _ ->
        VirtualFunction(Engine.module.address + offset, T::class, this.deref().address)
    }

inline fun <reified T : Any> Addressed.vtable(index: Int = 0) =
    ReadOnlyProperty<Any, VirtualFunction<T>> { _, _ ->
        VirtualFunction((this.deref().deref().address + (index * 8L)).toAddress().deref().address, T::class, this.deref().address)
    }