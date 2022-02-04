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

import org.spectralpowered.natives.offset.invoke
import org.spectralpowered.engine.Engine.module
import org.spectralpowered.natives.offset.get
import kotlin.reflect.full.memberProperties

object Offsets {

    /**
     * ===== GLOBAL OFFSETS =====
     */

    val dwLoginStep by module(2, 8)(0xC7, 0x05, 0[8], 0x48, 0x8B, 0xCF, 0x48, 0x83, 0xC4, 0[1])

    /**
     * ===== FUNCTION OFFSETS =====
     */

    internal fun scan() {
        val fields = this::class.memberProperties.toList()
        var resolvedCount = 0

        println("Scanning for ${fields.size} pointer signatures...")

        fields.forEach { field ->
            val name = field.name
            val resolvedAddress = field.getter.call(this) as Long
            if(resolvedAddress != 0L) {
                println("Resolved signature offset: [Name: $name, offset: 0x${resolvedAddress.toString(16)}].")
                resolvedCount++
            }
        }

        println("Successfully resolved $resolvedCount signature offsets.")
    }
}