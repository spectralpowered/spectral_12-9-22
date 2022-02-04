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

package org.spectralpowered.natives.memory

import com.sun.jna.Platform
import com.sun.jna.platform.win32.WinNT
import org.spectralpowered.natives.memory.windows.api.Windows
import java.util.*

/**
 * Attempts to open a process of the specified process ID.
 *
 * @param processID The ID of the process to open.
 */
@JvmOverloads
fun processByID(processID: Int, accessFlags: Int = WinNT.PROCESS_ALL_ACCESS): Process? = when {
    Platform.isWindows() || Platform.isWindowsCE() -> Windows.openProcess(processID, accessFlags)
    Platform.isMac() -> throw UnsupportedOperationException("Attaching to MacOS processes not yet supported.")
    Platform.isLinux() -> throw UnsupportedOperationException("Attaching to linux processes not yet supported.")
    else -> null
}

/**
 * Attempts to open a process of the specified process name.
 *
 * @param processName The name of the process to open.
 */
@JvmOverloads
fun processByName(processName: String, accessFlags: Int = WinNT.PROCESS_ALL_ACCESS): Process? = when {
    Platform.isWindows() || Platform.isWindowsCE() -> Windows.openProcess(processName, accessFlags)
    Platform.isLinux() -> {
        val search = Runtime.getRuntime().exec(arrayOf("bash", "-c",
            "ps -A | grep -m1 \"$processName\" | awk '{print $1}'"))
        val scanner = Scanner(search.inputStream)
        val processID = scanner.nextInt()
        scanner.close()
        processByID(processID)
    }
    else -> null
}