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

package org.spectralpowered.runescape.api

import org.jire.arrowhead.Addressed
import org.spectralpowered.api.Client
import org.spectralpowered.natives.jvm.Offsets.dwClientStates
import org.spectralpowered.natives.jvm.Offsets.dwLoginPage
import org.spectralpowered.natives.jvm.SpectralNatives.module
import org.spectralpowered.natives.jvm.SpectralNatives.process
import org.spectralpowered.natives.jvm.util.invoke
import org.spectralpowered.natives.jvm.util.pointer

class RSClient(override val address: Long = module.address) : Addressed, Client {

    override var gameState: Int by process(process.pointer(address + dwClientStates, 0x1F98))

    override var loginPage: Int by process(address + dwLoginPage)

}