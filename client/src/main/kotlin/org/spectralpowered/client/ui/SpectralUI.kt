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

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme
import com.sun.jna.platform.win32.WinDef
import org.spectralpowered.client.Spectral
import org.spectralpowered.common.inject
import javax.swing.JDialog
import javax.swing.JFrame

class SpectralUI {

    private val spectral: Spectral by inject()

    lateinit var osrsHwnd: WinDef.HWND internal set

    internal lateinit var clientFrame: ClientFrame private set
    internal lateinit var overlayFrame: OverlayFrame private set

    fun open() {
        /*
         * Install the Spectral Powered Look-And-Feel Theme.
         */
        JFrame.setDefaultLookAndFeelDecorated(true)
        JDialog.setDefaultLookAndFeelDecorated(true)
        FlatAtomOneDarkContrastIJTheme.setup()

        /*
         * Open the Spectral client frame.
         */
        clientFrame = ClientFrame()

        /*
         * Open the Spectral client overlay frame.
         */
        overlayFrame = OverlayFrame(clientFrame).also { it.open() }
    }

    fun close() {
        clientFrame.isVisible = false
        overlayFrame.isVisible = false
        spectral.stop()
    }
}