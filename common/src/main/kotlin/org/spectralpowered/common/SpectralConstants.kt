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

package org.spectralpowered.common

import java.io.File

/**
 * SPECTRAL PATH CONSTANTS
 */

val SPECTRAL_BASE_DIR = File(System.getProperty("user.home")).resolve(".spectral/")
val SPECTRAL_JVM_DIR = SPECTRAL_BASE_DIR.resolve("jvm/")
val SPECTRAL_BIN_DIR = SPECTRAL_BASE_DIR.resolve("bin/")
val SPECTRAL_LOG_DIR = SPECTRAL_BASE_DIR.resolve("logs/")
val SPECTRAL_PLUGIN_DIR = SPECTRAL_BASE_DIR.resolve("plugins/")

fun allSpectralDirs() = listOf(
    SPECTRAL_BASE_DIR,
    SPECTRAL_JVM_DIR,
    SPECTRAL_BIN_DIR,
    SPECTRAL_LOG_DIR,
    SPECTRAL_PLUGIN_DIR
)

/**
 * SPECTRAL URL CONSTANTS
 */

const val SPECTRAL_ADOPTJDK_11_JRE_URL = "https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.13%2B8/OpenJDK11U-jre_x64_windows_hotspot_11.0.13_8.zip"