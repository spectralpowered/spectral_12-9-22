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

package org.spectralpowered.engine

import org.jire.arrowhead.Module
import org.jire.arrowhead.Process
import org.jire.arrowhead.processByName
import org.spectralpowered.util.retry

class Engine {

    lateinit var process: Process private set
    lateinit var module: Module private set

    fun init() {
        println("Initializing Spectral engine.")

        /*
         * Attach to process.
         */
        retry(128L) {
            process = processByName("osclient.exe")!!
        }

        retry(128L) {
            process.loadModules()
            module = process.modules["osclient.exe"]!!
        }

        println("Successfully attached to process ID: ${process.id}.")
    }


}
