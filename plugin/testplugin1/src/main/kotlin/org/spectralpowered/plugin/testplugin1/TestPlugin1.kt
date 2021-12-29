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
import org.spectralpowered.api.LoginPage
import org.spectralpowered.common.every
import org.spectralpowered.plugin.PluginAttributes
import org.spectralpowered.plugin.SpectralPlugin
import org.tinylog.kotlin.Logger

class TestPlugin1(attributes: PluginAttributes) : SpectralPlugin(attributes) {

    override fun onEnable() {
        Logger.info("Test plugin fdsfsf Enabled running version: $version")

        every(1L) {
            if(Client.loginPage == LoginPage.STEAM_LOGIN || Client.loginPage == LoginPage.STEAM_LOGIN_SETUP) {
                Client.loginPage = LoginPage.NORMAL_LOGIN
            }
        }
    }

    override fun onDisable() {
        Logger.info("Test plugin Disabled!")
    }
}