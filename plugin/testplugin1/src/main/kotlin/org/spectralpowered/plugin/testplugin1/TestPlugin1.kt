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

package org.spectralpowered.plugin.testplugin1

import org.spectralpowered.api.Client
import org.spectralpowered.api.Console
import org.spectralpowered.api.Spectral
import org.spectralpowered.common.every
import org.spectralpowered.plugin.PluginAttributes
import org.spectralpowered.plugin.SpectralPlugin

class TestPlugin1(attributes: PluginAttributes) : SpectralPlugin(attributes) {

    override fun onEnable() {
        Console.info("Test Plugin enabled! [Version: $version].")

        Console.log("")
        Console.debug("This is a debug message.")
        Console.info("This is an info message.")
        Console.warn("This is a warning message.")
        Console.error("This is an error message.")

        every(1000L) {
            Console.info("<col=0000FF>GameState:</col> ${Client.gameState} <col=0000FF>LoginPage:</col> ${Client.loginPage}")
        }
    }

    override fun onDisable() {

    }
}