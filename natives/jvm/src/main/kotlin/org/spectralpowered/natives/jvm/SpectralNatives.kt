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

package org.spectralpowered.natives.jvm

import org.jire.arrowhead.Module
import org.jire.arrowhead.Process
import org.jire.arrowhead.processByID
import org.koin.core.parameter.parametersOf
import org.spectralpowered.common.get
import org.spectralpowered.common.retry
import org.spectralpowered.natives.jvm.api.RSClient
import org.tinylog.kotlin.Logger

object SpectralNatives {

    private lateinit var client: RSClient

    lateinit var process: Process private set
    lateinit var module: Module private set

    fun attachProcess(processId: Int) {
        Logger.info("Attaching to Old School RuneScape process ID: $processId.")

        retry(128L) {
            process = processByID(processId)!!
            process.loadModules()
            module = process.modules["osclient.exe"]!!
        }

        Logger.info("Successfully attached to process's memory address: 0x${module.address.toString(16)}.")

        /*
         * Scan and resolve all signatures to cache the offset address's.
         */
        Offsets.scanSignatures()

        /*
         * Initialize all native jvm api singletons with the base address of
         * the base module of the client process.
         */
        this.initApi()
    }

    private fun initApi() {
        client = get { parametersOf(this.module.address) }

        Logger.info("Spectral JVM API has been fully initialized.")
    }
}