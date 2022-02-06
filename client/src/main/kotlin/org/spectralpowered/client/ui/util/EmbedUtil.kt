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

package org.spectralpowered.client.ui.util

import com.sun.jna.Pointer
import com.sun.jna.platform.win32.BaseTSD
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.*
import org.spectralpowered.api.Console
import java.awt.Rectangle

/**
 * This utility class is responsible for changing the Old School RuneScape's JagOpenGLView owner
 * to the Spectral frame and handling all window message translation.
 */

private lateinit var jagOpenGlHwnd: WinDef.HWND
private lateinit var jagWindowHwnd: WinDef.HWND
private lateinit var spectralWindowHwnd: WinDef.HWND

private lateinit var defJagWndProc: BaseTSD.LONG_PTR
private lateinit var defCanvasWndProc: BaseTSD.LONG_PTR

fun embedOpenGL(
    jagHwnd: WinDef.HWND,
    spectralHwnd: WinDef.HWND,
    canvasHwnd: WinDef.HWND
) {
    Console.info("Embedding Old School RuneScape OpenGL context into window.")

    jagWindowHwnd = jagHwnd
    spectralWindowHwnd = spectralHwnd
    jagOpenGlHwnd = User32.GetWindow(jagWindowHwnd, WinDef.DWORD(GW_CHILD.toLong()))

    User32.SetParent(jagOpenGlHwnd, canvasHwnd)
    User32.ShowWindow(jagOpenGlHwnd, SW_SHOW)

    defCanvasWndProc = User32.GetWindowLongPtr(canvasHwnd, GWL_WNDPROC)
    User32.SetWindowLongPtr(canvasHwnd, GWL_WNDPROC, canvasWndProc)
}

private val canvasWndProc = WindowProc { hwnd, uMsg, wParam, lParam ->
    when(uMsg) {
        WM_SIZE -> {
            val info = WINDOWINFO()
            User32.GetWindowInfo(spectralWindowHwnd, info)

            val rect = Rectangle(
                info.rcWindow.left,
                info.rcWindow.top,
                info.rcWindow.right - info.rcWindow.left,
                info.rcWindow.bottom - info.rcWindow.top
            )
            User32.MoveWindow(jagWindowHwnd, rect.x, rect.y, rect.width, rect.height, false)
            User32.SetWindowLongPtr(jagWindowHwnd, GWL_EXSTYLE, Pointer((WS_EX_LAYERED or WS_EX_TRANSPARENT).toLong()))
            User32.SetLayeredWindowAttributes(jagWindowHwnd, 0, 0, LWA_ALPHA)
            User32.ShowWindow(jagWindowHwnd, SW_HIDE)
        }
    }
    return@WindowProc User32.CallWindowProc(defCanvasWndProc, hwnd, uMsg, wParam, lParam)
}

private const val GW_CHILD = 0x5
private const val PM_REMOVE = 0x0001