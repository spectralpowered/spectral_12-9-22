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
import com.sun.jna.Pointer
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Psapi
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.ptr.IntByReference
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
import org.spectralpowered.natives.memory.Process
import org.spectralpowered.natives.memory.windows.api.PsapiEx
import java.util.*

/**
 * Represents a process on Windows.
 */
class WindowsProcess(override val id: Int, val handle: WinNT.HANDLE) : Process {

    private val modulesMap = Collections.synchronizedMap(Object2ObjectArrayMap<String, WindowsModule>())

    override val address: Long = Pointer.nativeValue(handle.pointer)

    override fun loadModules() {
        modulesMap.clear()

        val modules = arrayOfNulls<WinDef.HMODULE>(4096) // support for Windows Creator's Update
        val needed = IntByReference()
        if (PsapiEx.EnumProcessModulesEx(handle, modules, modules.size, needed)) {
            for (i in 0..needed.value / Native.getNativeSize(WinDef.HMODULE::class.java)) {
                val hModule = modules[i] ?: continue
                val info = Psapi.MODULEINFO()
                if (Psapi.INSTANCE.GetModuleInformation(handle, hModule, info, info.size())) {
                    val address = Pointer.nativeValue(hModule.pointer)
                    val module = WindowsModule(address, this, hModule, info)
                    modulesMap[module.name] = module
                }
            }
        }
    }

    override val modules: Map<String, WindowsModule> = modulesMap

    override fun read(address: Pointer, data: Pointer, bytesToRead: Int)
            = Kernel32.INSTANCE.ReadProcessMemory(handle, address, data, bytesToRead, IntByReference(0))

    override fun write(address: Pointer, data: Pointer, bytesToWrite: Int)
            = Kernel32.INSTANCE.WriteProcessMemory(handle, address, data, bytesToWrite, IntByReference(0))

}