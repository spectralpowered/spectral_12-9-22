package org.spectralpowered.launcher

import org.spectralpowered.common.SpectralPaths
import org.spectralpowered.logger.Logger
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.util.zip.CRC32

object BinaryFileUpdater {

    fun run() {
        Logger.info("Checking Spectral binary files for update.")

        checkForUpdates("spectral.jar")
        checkForUpdates("spectral.dll")

        Logger.info("All Spectral binary files are up-to-date!")
    }

    private fun checkForUpdates(fileName: String) {
        val file = SpectralPaths.binDir.resolve(fileName)
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