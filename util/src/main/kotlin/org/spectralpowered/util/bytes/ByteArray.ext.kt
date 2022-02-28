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
 * Created by Kyle Escobar on 8/13/2016.
 */
val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

inline fun ByteArray.toStringHex(): String {
    val hexChars = CharArray(size * 2)
    var index = 0
    for (byte in this) {
        val ubyte = (byte.toInt() and 0xFF)
        hexChars[index * 2] = hexArray[ubyte ushr 4]
        hexChars[index * 2 + 1] = hexArray[ubyte and 0x0F]
        index++
    }
    return String(hexChars)
}

inline fun String.hexToByteArray(): ByteArray {
    val array = ByteArray(length / 2)
    for (i in array.indices) {
        array[i] = ((this[i * 2].toHexValue() shl 4) or (this[i * 2 + 1].toHexValue())).toByte()
    }
    return array
}

inline fun Char.toHexValue(): Int {
    return if (this >= 'a') {
        this - 'a' + 10
    } else if (this >= 'A')
        this - 'A' + 10
    else
        this - '0'
}