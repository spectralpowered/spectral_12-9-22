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

package org.spectralpowered.engine

import org.spectralpowered.engine.Engine.module
import org.spectralpowered.natives.offset.get
import org.spectralpowered.natives.offset.invoke
import kotlin.reflect.full.memberProperties

object Offsets {

    /**
     * ===== GLOBAL OFFSETS =====
     */

    val dwHHOOK by module(0)(0x48, 0x89, 0x0D, 0[4], 0x48, 0x8B, 0x9F, 0x08, 0x01, 0x00, 0x00)
    val dwLoginState by module(2, 8)(0xC7, 0x05, 0[8], 0x48, 0x8B, 0xCF, 0x48, 0x83, 0xC4, 0[1])
    val dwClient by module(3)(0x4C, 0x8B, 0x35, 0[4], 0x48, 0x8B, 0xF1)
    val dwConsole by module(3)(0x48, 0x8B, 0x8B, 0[4], 0x48, 0x8B, 0x01, 0xFF, 0x50, 0x08, 0x48, 0x8B, 0x8B, 0[4], 0x48, 0x8B, 0x01)
    /**
     * ===== FUNCTION OFFSETS =====
     */
    val fnConsoleWrite by module(1)(0xE8, 0[4], 0x48, 0x8D, 0x15, 0[4], 0x48, 0x8B, 0xCB, 0xE8, 0[4], 0x48, 0x8B, 0x44, 0x24, 0x30)
    val fnConsoleRegisterCommand by module(read = false)(0x48, 0x89, 0x5C, 0x24, 0x08, 0x48, 0x89, 0x74, 0x24, 0x10, 0x48, 0x89, 0x7C, 0x24, 0x18, 0x41, 0x56, 0x48, 0x83, 0xEC, 0[1], 0x49, 0x8B, 0xF8, 0x4C, 0x8B, 0xF1)

    fun scan() {
        val fields = this::class.memberProperties.toList()
        var resolvedCount = 0

        fields.forEach { field ->
            val name = field.name
            val resolvedAddress = field.getter.call(this) as Long
            if(resolvedAddress != 0L) {
                resolvedCount++
            }

            println("Found offset $name: 0x${resolvedAddress.toString(16)}")
        }
    }
}