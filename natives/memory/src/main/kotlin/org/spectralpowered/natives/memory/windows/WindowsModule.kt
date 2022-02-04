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

package org.spectralpowered.natives.memory.windows

import com.sun.jna.Native
import com.sun.jna.platform.win32.Psapi.MODULEINFO
import com.sun.jna.platform.win32.WinDef
import org.spectralpowered.natives.memory.Module
import org.spectralpowered.natives.memory.windows.api.PsapiEx

/**
 * Represents a module of a Windows process.
 */
class WindowsModule(override val address: Long, override val process: WindowsProcess,
                    val handle: WinDef.HMODULE, val info: MODULEINFO) : Module {

    override val name by lazy {
        val baseName = ByteArray(256)
        PsapiEx.GetModuleBaseNameA(process.handle, handle, baseName, baseName.size)
        Native.toString(baseName)!!
    }

    override val size = info.SizeOfImage.toLong()

}