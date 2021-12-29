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

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.*
import java.awt.*
import java.awt.SystemColor.window
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.lang.reflect.Method
import javax.swing.JComponent
import javax.swing.JWindow
import kotlin.concurrent.thread

class OverlayFrame(private val clientFrame: ClientFrame) : JWindow(clientFrame) {

    private val canvas = OverlayCanvas()
    private lateinit var hwnd: WinDef.HWND

    private var offsetBounds = Rectangle()

    init {
        preferredSize = clientFrame.preferredSize
        size = clientFrame.size
        add(canvas)
        background = Color(0, 0, 0, 0)
        isFocusable = false
    }

    var isWindowOpaque: Boolean
        get() {
            val awtUtilsClass = Class.forName("com.sun.awt.AWTUtilities")
            val method = awtUtilsClass.getMethod("getWindowOpaque")
            return method.invoke(null) as Boolean
        }
        set(value) {
            try {
                val awtUtilsClass = Class.forName("com.sun.awt.AWTUtilities")
                val method: Method =
                    awtUtilsClass.getMethod("setWindowOpaque", Window::class.java, Boolean::class.javaPrimitiveType)
                method.invoke(null, window, value)
            } catch (e: Exception) {}
        }

    var isTransparent: Boolean = false
        set(value) {
            var style = User32.INSTANCE.GetWindowLong(hwnd, GWL_EXSTYLE)
            style = when(value) {
                true -> style or WS_EX_LAYERED or WS_EX_TRANSPARENT
                false -> style or (WS_EX_LAYERED or WS_EX_TRANSPARENT).inv()
            }
            User32.INSTANCE.SetWindowLong(hwnd, GWL_EXSTYLE, style)
            field = value
        }

    fun open() {
        isVisible = true
        hwnd = WinDef.HWND(Native.getComponentPointer(this))
        isWindowOpaque = false
        isTransparent = true

        registerListeners()
        startOverlay()
    }

    private fun startOverlay() {
        /*
         * Start the overlay rendering thread.
         */
        thread {
            while(isVisible) {
                canvas.repaint()
                Thread.sleep(8L)
            }
        }
    }

    private fun registerListeners() {
        clientFrame.addComponentListener(object : ComponentAdapter() {
            override fun componentMoved(e: ComponentEvent) = this@OverlayFrame.updateSizeAndPosition(e)
            override fun componentResized(e: ComponentEvent) = this@OverlayFrame.updateSizeAndPosition(e)
        })
    }

    private fun updateSizeAndPosition(e: ComponentEvent) {
        val pos = clientFrame.contentPane.locationOnScreen
        val size = clientFrame.contentPane.size
        bounds = Rectangle(pos, size)
    }

    inner class OverlayCanvas : JComponent() {

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)

            val g2d = g.create() as Graphics2D

            /*
             * Clear the overlay canvas's screen from
             * the previous frame.
             */
            g2d.color = Color(0, 0, 0, 0)
            g2d.fillRect(0, 0, width, height)
            g2d.color = Color.WHITE

            /*
             * Draw something on the overlay
             */
            g2d.color = Color.RED
            g2d.fillRect(0, 0, 300, 175)

            /*
             * Dispose this frame's graphics context to flush the graphics buffer to
             * the canvas to be rendered.
             */
            g2d.dispose()
        }
    }
}