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

package org.spectralpowered.natives.jvm

import org.spectralpowered.natives.jvm.SpectralNatives.module
import org.spectralpowered.natives.jvm.util.get
import org.spectralpowered.natives.jvm.util.invoke
import org.tinylog.kotlin.Logger
import kotlin.reflect.full.memberProperties

/**
 * Holds pointer signatures which are used to dynamically resolve
 */
object Offsets {

    /**
     * ===== DWORD / GLOBAL POINTER SIGNATURES =====
     */
    val dwLoginState by module(2)(0x89, 0x3D, 0[4], 0xE9, 0[4], 0x48, 0x8B, 0x03)
    val dwStates by module(3)(0x48, 0x8B, 0x05, 0[4], 0x48, 0x8B, 0x88, 0[4], 0x48, 0x83, 0xC1, 0x08)


    /**
     * ===== FUNCTION POINTER SIGNATURES =====
     */
    val fnShowNormalLoginScreen by module(read = false)(0x40, 0x53, 0x48, 0x83, 0xEC, 0x30, 0x0F, 0xB6, 0xD9)

    internal fun scanSignatures() {
        val fields = this::class.memberProperties.toList()
        var resolvedCount = 0

        Logger.info("Scanning for ${fields.size} pointer signatures...")

        fields.forEach { field ->
            val name = field.name
            val resolvedAddress = field.getter.call(this) as Long
            if(resolvedAddress != 0L) {
                Logger.info("Resolved signature offset: [Name: $name, offset: 0x${resolvedAddress.toString(16)}].")
                resolvedCount++
            }
        }

        Logger.info("Successfully resolved $resolvedCount signature offsets.")
    }
}