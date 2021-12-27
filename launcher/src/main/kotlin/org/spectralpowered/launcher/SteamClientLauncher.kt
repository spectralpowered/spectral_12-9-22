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

import org.spectralpowered.launcher.splashscreen.SplashScreen
import org.tinylog.kotlin.Logger
import java.awt.Desktop
import java.io.InputStreamReader
import java.net.URI
import kotlin.system.exitProcess

object SteamClientLauncher {

    private const val OSRS_STEAM_APP_ID = 1343370
    private const val OSRS_STEAM_PROCESS_NAME = "osclient.exe"
    private const val OSRS_STEAM_WINDOW_TITLE = "Old School RuneScape"

    var processId: Int = -1
        private set

    fun run() {
        Logger.info("Launching Old School RuneScape Steam client process.")
        SplashScreen.progress = 0.65
        SplashScreen.progressText = "Launching Old School RuneScape Steam client."

        val protocol = URI("steam://run/$OSRS_STEAM_APP_ID")
        Desktop.getDesktop().browse(protocol)

        /*
         * Wait until windows successfully starts the process.
         */
        SplashScreen.progress = 0.75
        SplashScreen.progressText = "Waiting for Old School RuneScape to start."
        val startTime = System.currentTimeMillis()
        while(true) {
            if(System.currentTimeMillis() - startTime >= 30000) {
                Logger.error("Failed to start the Old School RuneScape Steam client within the 30 second timeout.")
                exitProcess(0)
            }
            val proc = Runtime.getRuntime()
                .exec(arrayOf("cmd", "/c", "tasklist /FI \"IMAGENAME eq $OSRS_STEAM_PROCESS_NAME\""))
            proc.waitFor()
            val lines = proc.inputStream.let { InputStreamReader(it) }.readText().split("\n")
            if(lines.any { it.contains(OSRS_STEAM_PROCESS_NAME) }) {
                break
            }
        }

        Logger.info("Found Old School RuneScape Steam client process.")
        SplashScreen.progressText = "Found Old School RuneScape Steam process."
    }
}