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

package org.spectralpowered.client.ui

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.*
import org.spectralpowered.common.inject
import java.awt.Canvas
import java.awt.Frame
import java.awt.Graphics
import java.awt.event.WindowAdapter
import java.util.concurrent.atomic.AtomicBoolean

class NativeCanvas(val osrsHwnd: WinDef.HWND) : Canvas() {

    private val ui: SpectralUI by inject()

    private var attached = AtomicBoolean(false)

    var parentHwnd: WinDef.HWND? = null
    var localHwnd: WinDef.HWND? = null

    private var style = -1

    fun attach() {
        if(!attached.get()) {
            localHwnd = WinDef.HWND(Native.getComponentPointer(this))
            if(localHwnd != null) {
                parentHwnd = User32.INSTANCE.GetAncestor(osrsHwnd, GA_PARENT)
                attached.set(true)
                NativeCanvasLibrary.INSTANCE.embedWindow(parentHwnd!!, localHwnd!!)
            }
        }
    }

    override fun paint(g: Graphics) {
        if(!attached.get()) attach()
        NativeCanvasLibrary.INSTANCE.resizeWindow(parentHwnd!!, localHwnd!!)
    }

    interface NativeCanvasLibrary : Library {
        fun resizeWindow(targetHwnd: WinDef.HWND, parentHwnd: WinDef.HWND)
        fun embedWindow(targetHwnd: WinDef.HWND, parentHwnd: WinDef.HWND)

        companion object {
            val INSTANCE by lazy { Native.load("spectral", NativeCanvasLibrary::class.java) }
        }
    }

    companion object {
        private const val WS_EX_TOOLWINDOW = 0x00000080
        private const val WS_EX_APPWINDOW = 0x00040000
    }
}