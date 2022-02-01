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

package org.spectralpowered.natives.memory.platform.windows.api

import com.sun.jna.Native
import com.sun.jna.win32.W32APIOptions
import com.sun.jna.platform.win32.Psapi as JnaPsapi

interface Psapi : JnaPsapi {
    companion object : Psapi by Native.load("psapi", Psapi::class.java, W32APIOptions.DEFAULT_OPTIONS) as Psapi
}