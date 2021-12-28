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

package org.spectralpowered.api

import org.spectralpowered.common.bind
import org.spectralpowered.common.inject
import org.spectralpowered.natives.jvm.api.RSClient

/**
 * Holds states and functions which perform general operations for the
 * Old School RuneScape client.
 */
object Client {

    private val rsClient: RSClient by inject()

    /**
     * The client's state to track what cycle function it should be executing
     * every game tick.
     */
    var gameState: Int by bind(rsClient.states::gameState)

    /**
     * The login screen's state which determines the display screen in the login box.
     */
    var loginState: Int by bind(rsClient::loginState)

}