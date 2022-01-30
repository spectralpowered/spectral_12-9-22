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

import java.awt.Dimension
import javax.swing.JFrame

/**
 * Represents the main 'Spectral' client object which is the start/entrypoint to the injected api and plugin
 * components / internals.
 */
class Spectral {

    companion object {

        /**
         * The main static entrypoint which get invoked by the Spectral launcher bootstrap process. This then initializes,
         * and creates the [Spectral] singleton object within the dependency injection framework.
         */
        @JvmStatic
        fun start() {
            val frame = JFrame("Test")
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.preferredSize = Dimension(500, 500)
            frame.size = frame.preferredSize
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}