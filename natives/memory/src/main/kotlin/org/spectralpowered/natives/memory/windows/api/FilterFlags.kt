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

package org.spectralpowered.natives.memory.windows.api


/**
 * Filter flags used for module listing.
 */
object FilterFlags {

    /**
     * List the 32-bit modules.
     */
    const val LIST_MODULES_32BIT = 0x01

    /**
     * List the 64-bit modules.
     */
    const val LIST_MODULES_64BIT = 0x02

    /**
     * List all modules.
     */
    const val LIST_MODULES_ALL = 0x03

    /**
     * Use the default behavior.
     */
    const val LIST_MODULES_DEFAULT = 0x0

}