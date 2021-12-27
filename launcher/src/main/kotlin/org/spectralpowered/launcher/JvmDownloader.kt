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

import org.spectralpowered.common.SPECTRAL_ADOPTJDK_11_JRE_URL
import org.spectralpowered.common.SPECTRAL_JVM_DIR
import org.spectralpowered.launcher.splashscreen.SplashScreen
import org.tinylog.kotlin.Logger
import java.io.FileOutputStream
import java.net.URL
import java.util.zip.ZipFile

object JvmDownloader {

    private const val JRE_SUBFOLDER_NAME = "jdk-11.0.13+8-jre"

    fun run() {
        SplashScreen.progress = 0.25
        SplashScreen.progressText = "Checking Spectral JVM files."
        if(SPECTRAL_JVM_DIR.listFiles()?.isEmpty() != false) {
            Logger.info("Downloading AdoptJDK 11 JRE for Spectral...")
            SplashScreen.progressText = "Downloading AdoptJDK 11 JRE."

            val bytes = URL(SPECTRAL_ADOPTJDK_11_JRE_URL).openConnection().getInputStream().readAllBytes()
            FileOutputStream(SPECTRAL_JVM_DIR.resolve("adoptjdk11-jre.zip")).use {
                it.write(bytes)
            }

            Logger.info("Extracting AdoptJDK 11 JRE archive...")
            SplashScreen.progressText = "Extracting AdoptJDK 11 JRE archive."
            ZipFile(SPECTRAL_JVM_DIR.resolve("adoptjdk11-jre.zip")).use { zip ->
                zip.entries().asSequence().forEach { entry ->
                    val entryName = entry.name.replace("${JRE_SUBFOLDER_NAME}/", "")
                    if(entry.isDirectory) {
                        if(entryName.isBlank() || entryName == JRE_SUBFOLDER_NAME) return@forEach
                        else SPECTRAL_JVM_DIR.resolve(entryName).mkdirs()
                    } else {
                        zip.getInputStream(entry).use { input ->
                            SPECTRAL_JVM_DIR.resolve(entryName).outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }
            SPECTRAL_JVM_DIR.resolve("adoptjdk11-jre.zip").deleteRecursively()
        }
    }
}