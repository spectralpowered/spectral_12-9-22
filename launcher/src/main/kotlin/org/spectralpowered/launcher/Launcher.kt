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
import org.spectralpowered.common.allSpectralDirs
import org.spectralpowered.launcher.splashscreen.SplashScreen
import org.spectralpowered.launcher.util.Injector
import org.spectralpowered.logger.Logger

object Launcher {

    const val DEVELOP_MODE = true

    @JvmStatic
    fun main(args: Array<String>) = SplashScreen.open()

    fun launch() {
        Logger.info("Preparing to launch Spectral.")

        this.checkDirs()
        JvmDownloader.run()
        BinaryFileUpdater.run()
        SteamClientLauncher.run()
        this.injectDlls()

        Logger.info("Spectral has successfully launched. Exiting launcher process.")
        SplashScreen.progress = 1.0
        SplashScreen.progressText = "Spectral has successfully completed launch."
    }

    private fun checkDirs() {
        SplashScreen.progress = 0.1
        SplashScreen.progressText = "Checking required directories."

        allSpectralDirs().forEach { dir ->
            if(!dir.exists()) {
                Logger.info("Creating missing Spectral directory: ${dir.path}.")
                SplashScreen.progressText = "Creating missing directory: ${dir.path}."
                dir.mkdirs()
            }
        }
    }

    private fun injectDlls() {
        Logger.info("Preparing to inject Spectral native DLLs into client process.")
        SplashScreen.progress = 0.85
        SplashScreen.progressText = "Preparing to inject Spectral DLLs."

        /*
         * Inject the Spectral native dependency DLLs first and then inject 'spectral.dll' last.
         */
        Injector.injectDLL("osclient.exe", SPECTRAL_BIN_DIR.resolve("spectral.dll"))

        Logger.info("Successfully injected all Spectral DLLs.")
    }
}