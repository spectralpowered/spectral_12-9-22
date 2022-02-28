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

package org.spectralpowered.util.lambda

/**
 *
 * Created by Kyle Escobar on 9/2/16.
 */
class CooldownLambda(val time: Long, val inner: () -> Unit) : () -> Unit {
    var lastTime = 0L
    override fun invoke() {
        val now = System.currentTimeMillis()
        if (now - lastTime > time) {
            inner()
            lastTime = now
        }
    }
}

inline fun (() -> Unit).cooldown(time: Long): () -> Unit = CooldownLambda(time, this)