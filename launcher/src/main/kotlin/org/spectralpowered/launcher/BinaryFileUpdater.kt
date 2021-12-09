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

import org.spectralpowered.common.SPECTRAL_BIN_DIR
import org.spectralpowered.launcher.splashscreen.SplashScreen
import org.spectralpowered.logger.Logger
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.zip.CRC32

object BinaryFileUpdater {

    fun run() {
        Logger.info("Checking Spectral binary files for update.")
        SplashScreen.progress = 0.5
        SplashScreen.progressText = "Checking files for updates."

        /*
         * Check for updates for each binary file which is
         * distributed either in the launcher's JAR or downloaded
         * remotely if not running in a development environment.
         */
        checkForUpdates("spectral.dll")
        checkForUpdates("spectral.jar")

        Logger.info("All Spectral binary files are up-to-date!")
        SplashScreen.progressText = "All files are up-to-date."
    }

    private fun checkForUpdates(fileName: String) {
        val file = SPECTRAL_BIN_DIR.resolve(fileName)
        val latestBytes = readResource(fileName)

        if(!file.exists()) {
            file.update(latestBytes)
        }

        val curBytes = file.readBytes()

        val latestChecksum = latestBytes.checksum()
        val curChecksum = curBytes.checksum()

        if(latestChecksum != curChecksum) {
            file.update(latestBytes)
        }
    }

    private fun File.update(bytes: ByteArray) {
        Logger.info("Updating Spectral binary file: ${this.path}.")
        SplashScreen.progressText = "Updating file: ${this.path}."
        this.deleteRecursively()
        FileOutputStream(this).use { it.write(bytes) }
    }

    private fun readResource(file: String): ByteArray = BinaryFileUpdater::class.java.getResourceAsStream("/bin/$file")?.readAllBytes()
        ?: throw FileNotFoundException("Failed to find embedded resource file: /bin/$file.")

    private fun ByteArray.checksum(): Long {
        val crc = CRC32()
        crc.update(this, 0, this.size)
        return crc.value
    }
}