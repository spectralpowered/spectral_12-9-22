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

package org.spectralpowered.natives.memory.platform.windows

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Psapi.MODULEINFO
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.ptr.IntByReference
import org.spectralpowered.natives.memory.Module
import org.spectralpowered.natives.memory.Process
import org.spectralpowered.natives.memory.platform.windows.api.Kernel32
import org.spectralpowered.natives.memory.platform.windows.api.Psapi

class WindowsProcess(override val id: Int, val handle: WinNT.HANDLE) : Process {

    override val modules = mutableMapOf<String, WindowsModule>()

    override fun loadModules() {
        modules.clear()

        val hModules = arrayOfNulls<WinDef.HMODULE>(4096)
        val needed = IntByReference()
        if(Psapi.EnumProcessModulesEx(handle, hModules, hModules.size, needed)) {
            for(i in 0..needed.value / Native.getNativeSize(WinDef.HMODULE::class.java)) {
                val hModule = hModules[i] ?: continue
                val info = MODULEINFO()
                if(Psapi.GetModuleInformation(handle, hModule, info, info.size())) {
                    val address = Pointer.nativeValue(hModule.pointer)
                    val module = WindowsModule(address, this, hModule, info)
                    modules[module.name] = module
                }
            }
        }
    }

    override fun read(address: Pointer, data: Pointer, length: Int): Boolean
        = Kernel32.ReadProcessMemory(handle, address, data, length, IntByReference(0))

    override fun write(address: Pointer, data: Pointer, length: Int): Boolean
        = Kernel32.WriteProcessMemory(handle, address, data, length, IntByReference(0))
}