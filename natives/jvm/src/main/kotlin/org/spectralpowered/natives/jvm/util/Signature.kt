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

import com.sun.jna.Memory
import com.sun.jna.Pointer
import it.unimi.dsi.fastutil.bytes.ByteArrayList
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.jire.arrowhead.Addressed
import org.jire.arrowhead.Module
import org.jire.arrowhead.Source
import org.jire.arrowhead.unsign
import kotlin.reflect.KProperty

fun Source.uint(address: Long, offset: Long = 0L) = int(address, offset).unsign()

class Signature(
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
        while(currentAddress < offset) {
            if(memory.checkPattern(currentAddress, pattern)) {
                currentAddress += module.address + patternOffset
                if(read) currentAddress += module.process.uint(currentAddress)
                if(subtract) currentAddress -= module.address
                return@lazy when(read) {
                    true -> currentAddress + addressOffset
                    false -> currentAddress
                }
            }
            currentAddress++
        }
        throw RuntimeException("Failed to resolve offset for pattern: [${pattern.joinToString(" ") { "0x${it.toString(16) }" }}].")
    }

    var value = -1L

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Long {
        if(value == -1L) {
            value = address
        }
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) {
        this.value = value
    }

    private fun Pointer.checkPattern(offset: Long, pattern: ByteArray, skipZero: Boolean = true): Boolean {
        for(i in 0..pattern.lastIndex) {
            val value = pattern[i]
            if(skipZero && value.toInt() == 0) continue
            if(this.getByte(offset + i) != value) {
                return false
            }
        }
        return true
    }

    companion object {
        val memoryByModule = Object2ObjectArrayMap<Module, Memory>()

        private fun Signature.cachedMemory(): Memory {
            var memory = memoryByModule[module]
            if (memory == null) {
                memory = module.read(0, module.size.toInt(), fromCache = false)!!
                memoryByModule[module] = memory
            }
            return memory
        }
    }
}

class SignatureScan(
    val module: Module,
    val patternOffset: Long,
    val addressOffset: Long,
    val read: Boolean,
    val subtract: Boolean
) {

    private var signature: Signature? = null

    operator fun invoke(vararg mask: Any): Signature {
        if(this.signature == null) {
            val bytes = ByteArrayList()
            for(flag in mask) when(flag) {
                is Number -> bytes.add(flag.toByte())
                is RepeatedInt -> repeat(flag.repeats) { bytes.add(flag.value.toByte()) }
            }
            this.signature = Signature(module, patternOffset, addressOffset, read, subtract, bytes.toByteArray())
        }
        return this.signature!!
    }
}

operator fun Module.invoke(
    patternOffset: Long = 0L,
    addressOffset: Long = 4L,
    read: Boolean = true,
    subtract: Boolean = true
) = SignatureScan(
    this,
    patternOffset,
    addressOffset,
    read,
    subtract
)
