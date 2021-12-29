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

package org.spectralpowered.natives.jvm.api

import com.sun.jna.Function
import com.sun.jna.Pointer
import org.jire.arrowhead.Addressed
import org.spectralpowered.natives.jvm.Offsets.dwLoginState
import org.spectralpowered.natives.jvm.Offsets.dwStates
import org.spectralpowered.natives.jvm.Offsets.fnShowNormalLoginScreen
import org.spectralpowered.natives.jvm.SpectralNatives.process
import org.spectralpowered.natives.jvm.util.invoke
import org.spectralpowered.natives.jvm.util.pointer

class RSClient(override val address: Long) : Addressed {
    val states = RSStates(process.pointer(address + dwStates))

    var loginState: Int by process(address + dwLoginState)

    fun showNormalLoginScreen() = Function.getFunction(Pointer(address + fnShowNormalLoginScreen)).invoke(arrayOf(0))
}