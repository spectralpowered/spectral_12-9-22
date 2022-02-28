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

package org.spectralpowered.util.text

/**
 * Various extensions for String.
 * Created by Kyle Escobar on 6/1/16.
 */


val EmailRegularExpression: Regex = "[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?".toRegex(RegexOption.IGNORE_CASE)

inline fun String.isEmail(): Boolean {
    return matches(EmailRegularExpression)
}

fun String.toFloatMaybe(): Float? {
    try {
        return toFloat()
    } catch(e: NumberFormatException) {
        return null
    }
}

fun String.toDoubleMaybe(): Double? {
    try {
        return toDouble()
    } catch(e: NumberFormatException) {
        return null
    }
}

fun String.toIntMaybe(): Int? {
    try {
        return toInt()
    } catch(e: NumberFormatException) {
        return null
    }
}

fun String.toLongMaybe(): Long? {
    try {
        return toLong()
    } catch(e: NumberFormatException) {
        return null
    }
}

fun String.toFloatMaybe(default: Float): Float {
    try {
        return toFloat()
    } catch(e: NumberFormatException) {
        return default
    }
}

fun String.toDoubleMaybe(default: Double): Double {
    try {
        return toDouble()
    } catch(e: NumberFormatException) {
        return default
    }
}

fun String.toIntMaybe(default: Int): Int {
    try {
        return toInt()
    } catch(e: NumberFormatException) {
        return default
    }
}

fun String.toLongMaybe(default: Long): Long {
    try {
        return toLong()
    } catch(e: NumberFormatException) {
        return default
    }
}

fun String.toByte(radix: Int): Byte {
    return java.lang.Byte.parseByte(this, radix)
}

fun String.toShort(radix: Int): Short {
    return java.lang.Short.parseShort(this, radix)
}

fun String.toInt(radix: Int): Int {
    return java.lang.Integer.parseInt(this, radix)
}

fun String.toLong(radix: Int): Long {
    return java.lang.Long.parseLong(this, radix)
}