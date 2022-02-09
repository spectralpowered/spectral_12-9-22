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

package org.spectralpowered.engine

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinNT
import com.sun.jna.platform.win32.WinUser
import org.spectralpowered.natives.memory.Module
import org.spectralpowered.natives.memory.Process
import org.spectralpowered.natives.memory.processByName
import org.spectralpowered.util.retry


object Engine {

    lateinit var process: Process private set
    lateinit var module: Module private set

    fun init() {
        retry(128L) {
            process = processByName("osclient.exe")!!
        }

        retry(128L) {
            process.loadModules()
            module = process.modules["osclient.exe"]!!
        }

        /*
         * Scan and calculate all offset patterns.
         */
        Offsets.scan()

        /*
         * Run initial default setup actions on the attached process memory.
         */
        this.setup()
    }

    private fun setup() {
    }

    external fun unhookJagWindow(dwHHOOK: Long)
}
