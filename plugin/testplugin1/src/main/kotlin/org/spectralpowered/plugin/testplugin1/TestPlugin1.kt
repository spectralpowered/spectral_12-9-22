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

import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.tinylog.kotlin.Logger

class TestPlugin1(wrapper: PluginWrapper) : Plugin(wrapper) {

    override fun start() {
        Logger.info("Test plugin started!")
    }

    override fun stop() {
        Logger.info("Test plugin stopped!")
    }
}