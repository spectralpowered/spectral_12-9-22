package org.spectralpowered.client

import org.koin.core.context.startKoin
import org.spectralpowered.client.ui.SpectralUI
import org.spectralpowered.common.get
import org.spectralpowered.common.inject
import org.spectralpowered.logger.Logger
import kotlin.system.exitProcess

class Spectral {

    private val spectralUI: SpectralUI by inject()

    fun start() {
        Logger.info("Starting Spectral client.")

        /*
         * Start the Spectral UI.
         */
        spectralUI.open()
    }

    fun stop() {
        Logger.info("Stopping Spectral client.")
        spectralUI.close()
        exitProcess(0)
    }

    companion object {

        const val OSRS_STEAM_APPID = 1343370
        const val OSRS_STEAM_WINDOW_TITLE = "Old School RuneScape"
        const val OSRS_PROCESS_NAME = "osclient.exe"

        private val DI_MODULES = listOf(
            SPECTRAL_MODULE
        )

        @JvmStatic
        fun launch() {
            startKoin { modules(DI_MODULES) }

            /*
             * Start the Spectral client from the Singleton Spectral instance.
             */
            val spectral = get<Spectral>()

            try {
                spectral.start()
            } catch (e : Exception) {
                Logger.error(e) { "An error occurred in the Spectral client JVM." }
            }
        }
    }
}