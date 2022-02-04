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
import com.sun.jna.platform.win32.Kernel32
import com.sun.jna.platform.win32.Tlhelp32
import com.sun.jna.platform.win32.WinDef
import org.spectralpowered.natives.memory.windows.WindowsProcess

/**
 * Utility functions for dealing with native processes on Windows.
 */
object Windows {

    /**
     * Reusable DWORD of value zero; not intended to be mutated.
     */
    val DWORD_ZERO = WinDef.DWORD(0)

    /**
     * Opens a native process on Windows by the specified process ID, given the specified access flags.
     *
     * @param processID The process ID of the process to open.
     * @param accessFlags The access permission flags given to the process.
     */
    fun openProcess(processID: Int, accessFlags: Int): WindowsProcess {
        val handle = Kernel32.INSTANCE.OpenProcess(accessFlags, true, processID)
        return WindowsProcess(processID, handle)
    }

    /**
     * Opens a native process on Windows of the specified process name.
     *
     * @param processName The process name of the process to open.
     */
    fun openProcess(processName: String, accessFlags: Int): WindowsProcess? {
        val snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPALL, DWORD_ZERO)
        val entry = Tlhelp32.PROCESSENTRY32.ByReference() // we reuse the same entry during iteration
        try {
            while (Kernel32.INSTANCE.Process32Next(snapshot, entry)) {
                val fileName = Native.toString(entry.szExeFile)
                if (processName == fileName) return openProcess(entry.th32ProcessID.toInt(), accessFlags)
            }
        } finally {
            Kernel32.INSTANCE.CloseHandle(snapshot)
        }
        return null
    }

}