package org.spectralpowered.common

import java.io.File

object SpectralPaths {

    val baseDir = File(System.getProperty("user.home")).resolve("Spectral/")
    val binDir = baseDir.resolve("bin/")
    val logsDir = baseDir.resolve("logs/")
    val pluginsDir = baseDir.resolve("plugins/")
    val configsDir = baseDir.resolve("configs/")
    val jreDir = baseDir.resolve("jre/")

    fun allDirs() = listOf(baseDir, binDir, logsDir, pluginsDir, configsDir, jreDir)

    val osclientExe = binDir.resolve("osclient.exe")
    val steamApiDll = binDir.resolve("steam_api64.dll")
    val steamAppIdTxt = binDir.resolve("steam_appid.txt")
    val spectralJar = binDir.resolve("spectral.jar")
    val spectralDll = binDir.resolve("spectral.dll")

}