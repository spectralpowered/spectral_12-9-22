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

package org.spectralpowered.plugin

import org.pf4j.*
import org.spectralpowered.common.SPECTRAL_PLUGIN_DIR
import org.tinylog.kotlin.Logger
import java.nio.file.Paths

class PluginManager : DefaultPluginManager(listOf(SPECTRAL_PLUGIN_DIR.toPath())) {

    override fun createPluginDescriptorFinder(): CompoundPluginDescriptorFinder {
        return CompoundPluginDescriptorFinder().add(ManifestPluginDescriptorFinder())
    }

    fun loadAllPlugins() {
        Logger.info("Loading Spectral plugins.")

        this.loadPlugins()
        this.startPlugins()

        Logger.info("Successfully enabled ${this.startedPlugins.size} plugins.")
    }
}