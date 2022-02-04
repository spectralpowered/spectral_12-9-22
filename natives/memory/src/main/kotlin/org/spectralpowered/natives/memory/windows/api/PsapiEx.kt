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

package org.spectralpowered.natives.memory.windows.api

import com.sun.jna.Native
import com.sun.jna.platform.win32.Psapi
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.ptr.IntByReference
import com.sun.jna.win32.W32APIOptions
import java.lang.System.load

interface PsapiEx : Psapi {
    fun EnumProcessModulesEx(hProcess: WinNT.HANDLE, lphModule: Array<WinDef.HMODULE?>, cb: Int, lpcNeeded: IntByReference, dwFilterFlag: Int = FilterFlags.LIST_MODULES_ALL): Boolean
    fun GetModuleBaseNameA(hProcess: WinNT.HANDLE, hModule: WinDef.HMODULE, lpBaseName: ByteArray, nSize: Int): Int

    companion object : PsapiEx by Native.load("psapi", PsapiEx::class.java, W32APIOptions.DEFAULT_OPTIONS) as PsapiEx
}