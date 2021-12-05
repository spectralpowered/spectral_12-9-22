package org.spectralpowered.logger

import org.tinylog.Level
import org.tinylog.core.TinylogLoggingProvider
import org.tinylog.format.MessageFormatter
import org.tinylog.provider.ContextProvider
import org.tinylog.provider.LoggingProvider

class SpectralLoggingProvider : LoggingProvider {

    private var provider = TinylogLoggingProvider()

    override fun getContextProvider(): ContextProvider = provider.contextProvider

    override fun getMinimumLevel(): Level  = provider.minimumLevel

    override fun getMinimumLevel(tag: String?): Level = provider.getMinimumLevel(tag)

    override fun isEnabled(depth: Int, tag: String?, level: Level): Boolean = provider.isEnabled(depth + 1, tag, level)

    override fun log(depth: Int, tag: String?, level: Level?, exception: Throwable?, formatter: MessageFormatter?, obj: Any?, vararg arguments: Any?) {
        return provider.log(depth + 1, tag, level, exception, formatter, obj, arguments)
    }

    override fun log(loggerClassName: String?, tag: String?, level: Level?, exception: Throwable?, formatter: MessageFormatter?, obj: Any?, vararg arguments: Any?) {
        throw UnsupportedOperationException("Not yet supported.")
    }

    override fun shutdown() {
        throw UnsupportedOperationException("Not yet supported.")
    }
}