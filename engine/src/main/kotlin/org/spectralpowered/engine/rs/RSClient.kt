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

package org.spectralpowered.engine.rs

import org.spectralpowered.engine.Engine
import org.spectralpowered.engine.Offsets.dwClient
import org.spectralpowered.engine.Offsets.dwLoginState
import org.spectralpowered.engine.memory.field
import org.spectralpowered.engine.memory.get
import org.spectralpowered.engine.memory.global
import org.spectralpowered.natives.memory.Addressed

object RSClient : Addressed {
    override val address = Engine.module.address + dwClient

    var gameState: Int by field(0x1F98)
    var loginState: Int by global(dwLoginState)

    var baseX: Int by field(0xCD74)
    var baseY: Int by field(0xCD78)

    var renderSelf: Boolean by field(0xC959)

    val localPlayer = RSPlayer(this[0xC948])
    val console = RSConsole(this[0x587B50])

}