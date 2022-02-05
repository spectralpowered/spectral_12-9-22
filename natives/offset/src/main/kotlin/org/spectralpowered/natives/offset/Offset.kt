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

import com.sun.jna.Memory
import com.sun.jna.Pointer
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.spectralpowered.natives.memory.Addressed
import org.spectralpowered.natives.memory.Module
import org.spectralpowered.natives.memory.ext.uint
import kotlin.reflect.KProperty

class Offset(
    val module: Module,
    val patternOffset: Long,
    val addressOffset: Long,
    val read: Boolean,
    val subtract: Boolean,
    val pattern: ByteArray
) : Addressed {

    private val memory = cachedMemory()

    override val address by lazy(LazyThreadSafetyMode.NONE) {
        val offset = module.size - pattern.size

        var currentAddress = 0L
        while (currentAddress < offset) {
            if (memory.findPattern(currentAddress, pattern)) {
                currentAddress += module.address + patternOffset
                if (read) currentAddress += module.process.uint(currentAddress)
                if (subtract) currentAddress -= module.address
                return@lazy when(read) {
                    true -> currentAddress + addressOffset
                    false -> currentAddress
                }
            }
            currentAddress++
        }

        throw IllegalStateException("Failed to resolve offset for pattern [${pattern.joinToString(" ") { "0x${it.toString(16)}" }}]")
    }

    private var value = -1L

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        if (value == -1L)
            value = address
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        this.value = value
    }

    fun Pointer.findPattern(offset: Long, mask: ByteArray, skipZero: Boolean = true): Boolean {
        for (i in 0..mask.lastIndex) {
            val value = mask[i]
            if (skipZero && 0 == value.toInt()) continue
            if (value != getByte(offset + i))
                return false
        }
        return true
    }

    companion object {
        val memoryByModule = Object2ObjectArrayMap<Module, Memory>()

        private fun Offset.cachedMemory(): Memory {
            var memory = memoryByModule[module]
            if (memory == null) {
                memory = module.read(0, module.size.toInt(), fromCache = false)!!
                memoryByModule[module] = memory
            }
            return memory
        }
    }
}