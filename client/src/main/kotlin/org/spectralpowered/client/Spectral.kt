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
import org.spectralpowered.client.ui.SpectralUI
import org.spectralpowered.common.SpectralPaths
import org.spectralpowered.common.get
import org.spectralpowered.common.inject
import org.spectralpowered.engine.ENGINE_MODULE
import org.spectralpowered.engine.Engine
import java.io.File
import java.io.FileOutputStream
import java.nio.channels.Channels
import kotlin.system.exitProcess

/**
 * Represents the main 'Spectral' client object which is the start/entrypoint to the injected api and plugin
 * components / internals.
 */
class Spectral {

    private val engine: Engine by inject()
    private val ui: SpectralUI by inject()

    external fun helloFromRust(name: String)

    /**
     * Called when the Spectral client is started. Responsible for initializing and setting up
     * everything required for Spectral to work.
     */
    private fun start() {
        println("Starting Spectral client.")

        /*
         * Start the Spectral engine. This handles the JVM low level integration with the
         * OSRS client memory and process.
         */
        engine.init()

        /*
         * Launch the Spectral UI.
         */
        ui.open()

        helloFromRust("Kyle")
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