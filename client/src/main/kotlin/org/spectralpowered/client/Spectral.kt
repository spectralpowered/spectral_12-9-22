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

package org.spectralpowered.client

import org.koin.core.context.startKoin
import org.spectralpowered.api.Client
import org.spectralpowered.api.Console
import org.spectralpowered.client.ui.SpectralUI
import org.spectralpowered.common.SpectralPaths
import org.spectralpowered.common.get
import org.spectralpowered.common.inject
import org.spectralpowered.engine.ENGINE_MODULE
import org.spectralpowered.engine.Engine
import org.spectralpowered.util.every
import kotlin.system.exitProcess

/**
 * Represents the main 'Spectral' client object which is the start/entrypoint to the injected api and plugin
 * components / internals.
 */
class Spectral {

    private val ui: SpectralUI by inject()

    /**
     * Called when the Spectral client is started. Responsible for initializing and setting up
     * everything required for Spectral to work.
     */
    private fun start() {
        /*
         * Start the Spectral engine. This handles the JVM low level integration with the
         * OSRS client memory and process.
         */
        Engine.init()

        /*
         * Successfully initialized spectral client and attached to Old School
         * RuneScape client's process memory.
         */
        Console.info("Successfully initialized Spectral client on process ID: ${Engine.process.id}.")

        /*
         * Launch the Spectral UI.
         */
        ui.open()

        /*
         * Finished Spectral client startup.
         */
        Console.info("Spectral client has completed startup.")

        every(1000L) {
            Console.debug("BaseX: ${Client.baseX}, BaseY: ${Client.baseY}")
        }
    }

    /**
     * Stops or shuts down the Spectral client.
     */
    fun stop() {
        println("Stopping Spectral client.")
        exitProcess(0)
    }

    companion object {

        private val DI_MODULES = listOf(
            CLIENT_MODULE,
            ENGINE_MODULE
        )

        /**
         * The main static entrypoint which get invoked by the Spectral launcher bootstrap process. This then initializes,
         * and creates the [Spectral] singleton object within the dependency injection framework.
         */
        @JvmStatic
        fun main() {
            System.load(SpectralPaths.binDir.resolve("bootstrap.dll").absolutePath)

            /*
             * Start dependency injector.
             */
            startKoin { modules(DI_MODULES) }

            /*
             * Initialized the started Spectral client core.
             */
            get<Spectral>().start()
        }
    }
}