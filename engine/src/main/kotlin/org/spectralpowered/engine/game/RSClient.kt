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

package org.spectralpowered.engine.game

import org.spectralpowered.engine.Offsets.dwClient
import org.spectralpowered.engine.Offsets.dwLoginState
import org.spectralpowered.engine.util.field
import org.spectralpowered.engine.util.global
import org.spectralpowered.natives.memory.Addressed

open class RSClient(override val address: Long) : Addressed {
    var gameState: Int by field(0x1F98)
    var loginState: Int by global(dwLoginState)

    companion object : RSClient(dwClient)
}