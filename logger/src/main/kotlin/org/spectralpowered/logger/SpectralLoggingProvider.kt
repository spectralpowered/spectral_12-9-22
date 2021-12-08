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

package org.spectralpowered.logger

import org.tinylog.Level
import org.tinylog.core.TinylogLoggingProvider
import org.tinylog.format.MessageFormatter
import org.tinylog.provider.ContextProvider
import org.tinylog.provider.LoggingProvider

class SpectralLoggingProvider : LoggingProvider {

    private var provider = TinylogLoggingProvider()

    override fun getContextProvider(): ContextProvider = provider.contextProvider

    override fun getMinimumLevel(): Level = provider.minimumLevel

    override fun getMinimumLevel(tag: String?): Level = provider.getMinimumLevel(tag)

    override fun isEnabled(depth: Int, tag: String?, level: Level): Boolean = provider.isEnabled(depth + 1, tag, level)

    override fun log(
        depth: Int,
        tag: String?,
        level: Level?,
        exception: Throwable?,
        formatter: MessageFormatter?,
        obj: Any?,
        vararg arguments: Any?
    ) {
        return provider.log(depth + 1, tag, level, exception, formatter, obj, arguments)
    }

    override fun log(
        loggerClassName: String?,
        tag: String?,
        level: Level?,
        exception: Throwable?,
        formatter: MessageFormatter?,
        obj: Any?,
        vararg arguments: Any?
    ) {
        throw UnsupportedOperationException("Not yet supported.")
    }

    override fun shutdown() {
        throw UnsupportedOperationException("Not yet supported.")
    }
}