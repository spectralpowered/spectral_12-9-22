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

package org.spectralpowered.gradle.plugin.update4j

import java.util.*

class FolderSpec {
    var name: String? = null
        private set
    var base: String? = null
        private set
    var path: String? = null
        private set
    var isClasspath = false
        private set
    var isModulepath = false
        private set
    var isIgnoreBootConflict = false
        private set

    companion object {
        fun parse(descriptor: String?): FolderSpec {
            val folder = FolderSpec()
            val tokenizer = StringTokenizer(descriptor, "|")
            while (tokenizer.hasMoreTokens()) {
                val params = StringTokenizer(tokenizer.nextToken(), "=")
                when (params.nextToken()) {
                    "name" -> folder.name = params.nextToken()
                    "base" -> folder.base = params.nextToken()
                    "path" -> folder.path = params.nextToken()
                    "classpath" -> folder.isClasspath = true
                    "modulepath" -> folder.isModulepath = true
                    "ignorebootconflict" -> folder.isIgnoreBootConflict = true
                }
            }
            return folder
        }
    }
}