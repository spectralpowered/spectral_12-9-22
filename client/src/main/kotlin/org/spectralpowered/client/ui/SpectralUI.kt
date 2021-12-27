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
import javax.swing.SwingUtilities

class SpectralUI {

    private val spectral: Spectral by inject()

    lateinit var osrsHwnd: WinDef.HWND internal set
    private lateinit var clientFrame: ClientFrame

    fun open() {
        JFrame.setDefaultLookAndFeelDecorated(true)
        JDialog.setDefaultLookAndFeelDecorated(true)
        FlatAtomOneDarkContrastIJTheme.setup()

        SwingUtilities.invokeLater {
            clientFrame = ClientFrame()
            clientFrame.open()
        }
    }

    fun close() {
        clientFrame.close()
        spectral.stop()
    }
}