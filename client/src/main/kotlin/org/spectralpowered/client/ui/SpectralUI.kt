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

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.graphics.Color
import com.sun.jna.Native
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.*
import org.spectralpowered.client.ui.theme.SpectralTheme
import org.spectralpowered.client.ui.util.embedOpenGL
import org.spectralpowered.util.retry
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.SwingUtilities

class SpectralUI {

    lateinit var jagHwnd: WinDef.HWND private set
    lateinit var spectralHwnd: WinDef.HWND private set
    lateinit var canvasHwnd: WinDef.HWND private set

    private lateinit var frame: JFrame
    private lateinit var menuBar: MenuBar

    /**
     * Opens the Spectral client window UI and all included components. Once this is opened,
     * the Old School RuneScape Steam client attempts to be embedded into the program.
     */
    fun open() {
        println("Opening Spectral UI.")

        /*
         * Install the Spectral Theme for window.
         */
        SpectralTheme.install()

        SwingUtilities.invokeLater {
            this.initSwing()
            this.initCompose()
            this.resolveWindowHandles()

            /*
             * Embed the JagOpenGLView into the Spectral frame.
             */
            embedOpenGL(jagHwnd, spectralHwnd, canvasHwnd)
        }
    }

    private fun initSwing() {
        frame = JFrame("Spectral")
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.iconImages = ICONS
        frame.size = Dimension(1080, 800)
        frame.preferredSize = frame.size
        frame.minimumSize = Dimension(800, 600)
        frame.setLocationRelativeTo(null)
        frame.contentPane.layout = BorderLayout()

        menuBar = MenuBar()
        frame.jMenuBar = menuBar

        frame.isVisible = true
    }

    private fun initCompose() {
        val composePanel = ComposePanel()
        frame.contentPane.add(composePanel, BorderLayout.CENTER)
        composePanel.setContent { ComposeContent() }
    }

    @Composable
    @Preview
    fun ComposeContent() {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xFF21252B)),
            contentAlignment = Alignment.Center
        ) {
        }
    }

    private fun resolveWindowHandles() {
        /*
         * Find the JagWindow handle from the Windows desktop HWND.
         */
        retry(1L) {
            WindowUtils.getAllWindows(true).firstOrNull {
                it.title == "Old School RuneScape"
            }.let { jagHwnd = it!!.hwnd }
        }

        /*
         * Find the current Spectral frame HWND.
         */
        spectralHwnd = WinDef.HWND(Native.getComponentPointer(frame))
        canvasHwnd = User32.INSTANCE.GetWindow(spectralHwnd, WinDef.DWORD(GW_CHILD.toLong()))
    }

    companion object {

        /**
         * The application icons used for the window.
         */
        private val ICONS = listOf(
            "icon16.png",
            "icon32.png",
            "icon64.png",
            "icon128.png",
            "icon256.png",
            "icon512.png"
        ).map { ImageIcon(SpectralUI::class.java.getResource("/images/$it")).image }
    }
}