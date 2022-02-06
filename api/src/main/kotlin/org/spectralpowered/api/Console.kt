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

import org.spectralpowered.engine.rs.RSClient

object Console {

    /**
     * Writes a log message to the in-game development console.
     *
     * @param message String
     * @return Pointer
     */
    fun write(message: String) {
        RSClient.console.write(message)
    }

    fun trace(message: String) = write("<col=555555>[TRACE]</col> $message")
    fun debug(message: String) = write("<col=299999>[DEBUG]</col> $message")
    fun info(message: String) = write("<col=5394EC>[INFO]</col> $message")
    fun warn(message: String) = write("<col=A87B00>[WARN]</col> $message")
    fun error(message: String) = write("<col=CC666E>[ERROR]</col> $message")
    fun fatal(message: String) = write("<col=FF6B68>[FATAL]</col> $message")
}