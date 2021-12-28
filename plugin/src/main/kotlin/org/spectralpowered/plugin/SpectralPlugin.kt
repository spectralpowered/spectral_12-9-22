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

import org.pf4j.Plugin
import org.pf4j.PluginWrapper

typealias PluginAttributes = PluginWrapper

abstract class SpectralPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {

    val id: String get() = wrapper.descriptor.pluginId
    val mainClass: String get() = wrapper.descriptor.pluginClass
    val version: String get() = wrapper.descriptor.version
    val author: String get() = wrapper.descriptor.provider

    abstract fun onEnable()
    abstract fun onDisable()

    override fun start() { this.onEnable() }
    override fun stop() { this.onDisable() }
}