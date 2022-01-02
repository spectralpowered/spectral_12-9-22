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

package org.spectralpowered.runescape.api

import com.sun.jna.Function
import com.sun.jna.Pointer
import org.jire.arrowhead.Addressed
import org.spectralpowered.api.Console
import org.spectralpowered.natives.jvm.Offsets.dwConsolePtr
import org.spectralpowered.natives.jvm.Offsets.fnConsoleMessage
import org.spectralpowered.natives.jvm.SpectralNatives.module
import org.spectralpowered.natives.jvm.SpectralNatives.process
import org.spectralpowered.natives.jvm.util.pointer

class RSConsole : Console, Addressed {

    override val address: Long by process.pointer(module.address + dwConsolePtr, 0x88, 0x318, 0x250, 0x98, 0x8)

    override fun message(message: String) = Function.getFunction(Pointer(module.offset(fnConsoleMessage))).invoke(arrayOf(
        Pointer(address),
        message
    ))
}