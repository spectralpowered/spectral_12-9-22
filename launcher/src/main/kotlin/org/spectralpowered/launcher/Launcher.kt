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
import java.awt.Desktop
import java.io.InputStreamReader
import java.net.URI
import kotlin.system.exitProcess

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
         * Check if there is already an Old School RuneScape client process running. If so, terminate it before trying to
         * launch the Spectral client.
         */
        val proc = Runtime.getRuntime()
            .exec(arrayOf("cmd", "/c", "tasklist /FI \"IMAGENAME eq osclient.exe\""))
        proc.waitFor()
        val lines = proc.inputStream.let { InputStreamReader(it) }.readText().split("\n")
        if(lines.any { it.contains("osclient.exe") }) {
            Logger.info("Old School RuneScape client process is running! Terminating before launch.")

            Runtime.getRuntime()
                .exec(arrayOf("cmd", "/c", "taskkill /F /IM osclient.exe"))
                .waitFor()
            Thread.sleep(3000)
        }

        /*
         * Run initialization actions.
         */
        checkDirs()
        JreDownloader.run()
        BinaryFileDownloader.run()
    }

    /**
     * Starts the Old School RuneScape NXT client from Steam and integrates
     * everything for spectral to attach to its process.
     */
    private fun start() {
        Logger.info("Starting Spectral client...")

        /*
         * Launch the Old School RuneScape Steam NXT client.
         */
        startSteamClient()
        bootstrapSteamClient()

        Logger.info("Spectral launcher has completed. Exiting launcher process.")
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

    private fun startSteamClient() {
        Logger.info("Launching Old School RuneScape Steam client.")

        val protocol = URI("steam://run/1343370")
        Desktop.getDesktop().browse(protocol)

        /*
         * Wait until the process has started and is listed as running within windows.
         */
        val startTime = System.currentTimeMillis()
        while(true) {
            if(System.currentTimeMillis() - startTime >= 30000) {
                Logger.error("Failed to start the Old School RuneScape Steam client within 30 seconds. Exiting process.")
                exitProcess(0)
            }

            val proc = Runtime.getRuntime()
                .exec(arrayOf("cmd", "/c", "tasklist /FI \"IMAGENAME eq osclient.exe\""))
            proc.waitFor()
            val lines = proc.inputStream.let { InputStreamReader(it) }.readText().split("\n")
            if(lines.any { it.contains("osclient.exe") }) {
                break
            }
        }

        Logger.info("Successfully started client process through Steam.")
    }

    private fun bootstrapSteamClient() {
        /*
         * Inject the required Spectral client DLLs into the Old School RuneScape Steam
         * client process.
         */
        Injector.injectDLL("osclient.exe", SpectralPaths.jreDir.resolve("bin/server/jvm.dll"))
        Injector.injectDLL("osclient.exe", SpectralPaths.binDir.resolve("bootstrap.dll"))
    }
}