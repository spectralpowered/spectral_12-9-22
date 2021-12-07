package org.spectralpowered.client

import org.koin.core.context.startKoin
import org.spectralpowered.common.get
import org.spectralpowered.logger.Logger
import kotlin.system.exitProcess

class Spectral {

    fun start() {
        Logger.info("Starting Spectral client.")
    }

    fun stop() {
        Logger.info("Stopping Spectral client.")
        exitProcess(0)
    }

    companion object {

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