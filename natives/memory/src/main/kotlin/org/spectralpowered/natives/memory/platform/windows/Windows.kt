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
import com.sun.jna.platform.win32.Tlhelp32
import com.sun.jna.platform.win32.Tlhelp32.TH32CS_SNAPALL
import com.sun.jna.platform.win32.WinDef
import org.spectralpowered.natives.memory.platform.windows.api.Kernel32

object Windows  {

    fun openProcess(processId: Int, accessFlags: Int): WindowsProcess? {
        val handle = Kernel32.OpenProcess(accessFlags, true, processId)
        return WindowsProcess(processId, handle)
    }

    fun openProcess(processName: String, accessFlags: Int): WindowsProcess? {
        val snapshot = Kernel32.CreateToolhelp32Snapshot(TH32CS_SNAPALL, WinDef.DWORD(0))
        val entry = Tlhelp32.PROCESSENTRY32.ByReference()
        try {
            while(Kernel32.Process32Next(snapshot, entry)) {
                val fileName = Native.toString(entry.szExeFile)
                if(processName == fileName) {
                    return openProcess(entry.th32ProcessID.toInt(), accessFlags)
                }
            }
        } finally {
            Kernel32.CloseHandle(snapshot)
        }
        return null
    }
}