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

/**
 * The main Spectral client launch entrypoint. Responsible for setting up
 * and running Old School RuneScape and integrating Spectral into it's process.
 */
object Launcher {

    /**
     * Initialize and run setup/checks before launching the Spectral client startup
     * logic.
     */
    private fun init() {
        Logger.info("Initializing...")

        /*
         * Run initialization actions.
         */
        this.checkDirs()
        Updater.run()
    }

    /**
     * Starts the Old School RuneScape NXT client from Steam and integrates
     * everything for spectral to attach to its process.
     */
    private fun start() {
        Logger.info("Starting Spectral client...")


    }

    @JvmStatic
    fun main(args: Array<String>) {
        this.init()
        this.start()
    }

    private fun checkDirs() {
        SpectralPaths.allDirs.forEach { dir ->
            if(!dir.exists()) {
                Logger.info("Creating required directory: ${dir.path}.")
                dir.mkdirs()
            }
        }
    }
}