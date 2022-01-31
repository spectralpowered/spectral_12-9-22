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
import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipFile

object JreDownloader {

    private val OPENJDK_URL = URL("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.2%2B8/OpenJDK17U-jre_x64_windows_hotspot_17.0.2_8.zip")
    private const val rootFolderName = "jdk-17.0.2+8-jre"

    fun run() {
        if(SpectralPaths.jreDir.listFiles()?.isEmpty() != false) {
            Logger.info("Downloading OpenJDK 17 JRE...")
            val bytes = OPENJDK_URL.openConnection().getInputStream().readAllBytes()
            FileOutputStream(SpectralPaths.jreDir.resolve("jre.zip")).use {
                it.write(bytes)
            }

            Logger.info("Extracting OpenJDK 17 JRE archive...")
            ZipFile(SpectralPaths.jreDir.resolve("jre.zip")).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    val entryName = entry.name.replace("$rootFolderName/", "")
                    if(entry.isDirectory) {
                        if(entryName.isBlank() || entryName == rootFolderName) return@forEach
                        else SpectralPaths.jreDir.resolve(entryName).mkdirs()
                    } else {
                        zip.getInputStream(entry).use { input ->
                            SpectralPaths.jreDir.resolve(entryName).outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }

            SpectralPaths.jreDir.resolve("jre.zip").deleteRecursively()
        }
     }
}