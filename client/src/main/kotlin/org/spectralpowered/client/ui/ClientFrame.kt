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

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinUser
import org.spectralpowered.common.inject
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Rectangle
import javax.swing.ImageIcon
import javax.swing.JFrame

class ClientFrame : JFrame("Spectral") {

    private val ui: SpectralUI by inject()

    lateinit var canvas: NativeCanvas private set
    private val menuBar = MenuBar()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImages = ICONS
        size = Dimension(800, 600)
        preferredSize = size
        minimumSize = size
        layout = BorderLayout()
        jMenuBar = menuBar

        this.syncFrameSize()

        setLocationRelativeTo(null)
        isVisible = true

        this.initCanvas()
    }

    private fun initCanvas() {
        canvas = NativeCanvas(ui.osrsHwnd)
        add(canvas, BorderLayout.CENTER)
    }

    private fun syncFrameSize() {
        val info = WinUser.WINDOWINFO()
        User32.INSTANCE.GetWindowInfo(ui.osrsHwnd, info)

        val x = info.rcClient.left
        val y = info.rcClient.top
        val w = info.rcClient.right - info.rcClient.left
        val h = info.rcClient.bottom - info.rcClient.top

        bounds = Rectangle(x, y, w, h)
    }

    companion object {
        private val ICONS = listOf(
            "/images/icons/icon16.png",
            "/images/icons/icon32.png",
            "/images/icons/icon64.png",
            "/images/icons/icon128.png",
            "/images/icons/icon256.png",
            "/images/icons/icon512.png"
        ).map { ImageIcon(ClientFrame::class.java.getResource(it)).image }
    }
}