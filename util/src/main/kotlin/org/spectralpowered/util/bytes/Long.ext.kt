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

package org.spectralpowered.util.bytes

/**
 *
 * Created by shanethompson on 6/29/17.
 *
 */


class LongBitArray(var value: Long = 0) : Iterable<Boolean> {
    operator fun get(index: Int): Boolean {
        return value.ushr(index).and(0x1) == 1L
    }

    operator fun set(index: Int, isTrue: Boolean) {
        if(isTrue) {
            value = value or 0x1.shl(index).toLong()
        } else {
            value = value.and(1.shl(index).inv().toLong())
        }
    }

    override fun iterator(): Iterator<Boolean> {
        return object : Iterator<Boolean> {
            var currentIndex = 0
            override fun hasNext(): Boolean {
                return currentIndex < 64
            }

            override fun next(): Boolean {
                val result = this@LongBitArray[currentIndex]
                currentIndex++
                return result
            }
        }
    }
}

fun Long.toBitArray(): LongBitArray {
    return LongBitArray(this)
}