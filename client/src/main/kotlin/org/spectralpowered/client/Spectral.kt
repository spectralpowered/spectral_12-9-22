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

package org.spectralpowered.client

import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.ptr.IntByReference
import org.koin.core.context.startKoin
import org.spectralpowered.api.API_MODULE
import org.spectralpowered.client.ui.SpectralUI
import org.spectralpowered.common.get
import org.spectralpowered.common.inject
import org.spectralpowered.natives.jvm.JVM_NATIVES_MODULE
import org.spectralpowered.natives.jvm.SpectralNatives
import org.spectralpowered.plugin.PLUGIN_MODULE
import org.spectralpowered.plugin.PluginManager
import org.tinylog.kotlin.Logger
import java.lang.management.ManagementFactory
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class Spectral {

    private val ui: SpectralUI by inject()
    private val pluginManager: PluginManager by inject()

    var processId: Int = -1
        private set

    fun start() {
        Logger.info("Starting Spectral client.")

        this.waitForClientWindow()
        ui.open()

        /*
         * Attach the JVM to the native client process's memory space.
         */
        SpectralNatives.attachProcess(processId)

        /*
         * Load all Spectral client plugins.
         */
        pluginManager.loadPlugins()
        pluginManager.startPlugins()
    }

    fun stop() {
        Logger.info("Stopping Spectral client.")

        /*
         * Disable / Stop all loaded plugins.
         */
        pluginManager.stopPlugins()
        pluginManager.unloadPlugins()

        /*
         * Close all Spectral UI's
         */
        ui.close()

        exitProcess(0)
    }

    private fun waitForClientWindow() {
        Logger.info("Waiting for Old School RuneScape client window.")
        processId = ManagementFactory.getRuntimeMXBean().name.split("@").first().toInt()
        while(!Thread.interrupted()) {
            try {
                ui.osrsHwnd = WindowUtils.getAllWindows(true).first {
                    val pid = IntByReference()
                    User32.INSTANCE.GetWindowThreadProcessId(it.hwnd, pid)
                    it.title == "Old School RuneScape" && pid.value == processId
                }.hwnd
                break
            } catch (e : Exception) {
                Thread.sleep(0L)
            }
        }
        Logger.info("Found client window running with process ID: $processId.")
    }

    companion object {

        private val DI_MODULES = listOf(
            CLIENT_MODULE,
            API_MODULE,
            PLUGIN_MODULE,
            JVM_NATIVES_MODULE
        )

        @JvmStatic
        fun init() {
            startKoin { modules(DI_MODULES) }
            get<Spectral>().start()
        }
    }
}