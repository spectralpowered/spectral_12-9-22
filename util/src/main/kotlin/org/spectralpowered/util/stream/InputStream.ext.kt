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

package org.spectralpowered.util.stream

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.Charset

fun InputStream.toByteArray(): ByteArray {
    val output = ByteArrayOutputStream()
    try {
        val b = ByteArray(4096)
        var n = read(b)
        while (n != -1) {
            output.write(b, 0, n)
            n = read(b)
        }
        return output.toByteArray()
    } finally {
        output.close()
    }
}

fun InputStream.writeToFile(file: File) {
    val fos = FileOutputStream(file)
    try {
        val data = ByteArray(1024)
        var total: Long = 0
        while (true) {
            val count = this.read(data)
            if (count == -1) break
            total += count.toLong()
            fos.write(data, 0, count)
        }
        fos.flush()
    } finally {
        try {
            fos.close()
        } catch (e: Throwable) {/*squish*/
        }
        try {
            this.close()
        } catch (e: Throwable) {/*squish*/
        }
    }
}

fun InputStream.toString(charset: String): String {
    val output = ByteArrayOutputStream()
    try {
        val b = ByteArray(4096)
        var n = read(b)
        while (n != -1) {
            output.write(b, 0, n)
            n = read(b)
        }
        return output.toString(charset)
    } finally {
        output.close()
    }
}

fun InputStream.toString(charset: Charset): String {
    return toByteArray().toString(charset)
}