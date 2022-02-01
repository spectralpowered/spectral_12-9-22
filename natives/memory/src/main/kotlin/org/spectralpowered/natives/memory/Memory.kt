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

import com.sun.jna.platform.win32.WinNT.PROCESS_ALL_ACCESS
import org.spectralpowered.natives.memory.platform.windows.Windows

fun openProcessById(processId: Int, accessFlags: Int = PROCESS_ALL_ACCESS): Process?
    = Windows.openProcess(processId, accessFlags)

fun openProcessByName(processName: String, accessFlags: Int = PROCESS_ALL_ACCESS): Process?
    = Windows.openProcess(processName, accessFlags)