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

import org.spectralpowered.engine.memory.field
import org.spectralpowered.natives.memory.Addressed

class RSPlayer(override val address: Long) : Addressed {
    val x: Int by field(0x30)
    val y: Int by field(0x34)
    val rotation: Int by field(0x38)
    val cycle: Int by field(0x44)
    var idleAnimation: Int by field(0x48)
    var turnLeftAnimation: Int by field(0x4C)
    var turnRightAnimation: Int by field(0x50)
    var walkAnimation: Int by field(0x54)
    var walkBackAnimation: Int by field(0x58)
    var walkLeftAnimation: Int by field(0x5C)
    var walkRightAnimation: Int by field(0x60)
    var runAnimation: Int by field(0x64)
    var movementAnimation: Int by field(0x118)
    var movementAnimationCycle: Int by field(0x11C)
    var movementAnimationFrame: Int by field(0x120)
    var animation: Int by field(0x124)
    var animationCycle: Int by field(0x128)
    var animationFrame: Int by field(0x12C)
    var skullIcon: Int by field(0x398)
    var prayerIcon: Int by field(0x39C)
}