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

package org.spectralpowered.launcher

import org.spectralpowered.common.SpectralPaths
import org.tinylog.kotlin.Logger
import java.io.File
import java.io.FileOutputStream
import java.util.zip.CRC32
import kotlin.system.exitProcess

object Updater {

    fun run() {
        Logger.info("Checking files for updates.")

        /*
         * Check and update all required binary files.
         */
        update("spectral.jar")
        update("bootstrap.dll")

        Logger.info("All files are up-to-date.")
    }

    private fun update(filename: String) {
        val remoteBytes = filename.getResourceBytes()

        val file = SpectralPaths.binDir.resolve(filename)
        if(!file.exists()) {
            file.replaceBytes(remoteBytes)
        }

        val localBytes = SpectralPaths.binDir.resolve(filename).readBytes()

        val remoteCrc = remoteBytes.crc()
        val localCrc = localBytes.crc()

        if(localCrc != remoteCrc) {
            file.replaceBytes(remoteBytes)
        }
    }

    private fun File.replaceBytes(bytes: ByteArray) {
        Logger.info("Updating file: ${this.name}...")

        this.deleteRecursively()
        FileOutputStream(File(this.absolutePath)).use {
            it.write(bytes)
        }
    }

    private fun String.getResourceBytes(): ByteArray = try {
        Updater::class.java.getResourceAsStream("/bin/$this")?.readAllBytes()
            ?: throw IllegalArgumentException("Failed to read resource bytes from: /bin/$this")
    } catch(e : Exception) {
        Logger.error(e) { "An error occurred while reading embedded file data." }
        exitProcess(1)
    }

    private fun ByteArray.crc(): Long {
        val crc = CRC32()
        crc.update(this, 0, this.size)
        return crc.value
    }
}