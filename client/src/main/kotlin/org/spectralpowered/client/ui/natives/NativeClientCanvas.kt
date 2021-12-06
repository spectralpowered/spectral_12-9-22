package org.spectralpowered.client.ui.natives

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.platform.win32.WinUser.GA_PARENT
import java.awt.Canvas
import java.awt.Graphics
import java.util.concurrent.atomic.AtomicBoolean

class NativeClientCanvas(private val osrsHandle: WinDef.HWND) : Canvas() {
    private var parentHandle: WinDef.HWND? = null
    private var selfHandle: WinDef.HWND? = null

    val attached = AtomicBoolean(false)

    private fun attach() {
        if(!attached.get()) {
            selfHandle = WinDef.HWND(Native.getComponentPointer(this))
            if(selfHandle != null) {
                parentHandle = User32.INSTANCE.GetAncestor(osrsHandle, GA_PARENT)
                attached.set(true)
                WindowEmbed.INSTANCE.embedWindow(osrsHandle, selfHandle!!)
                repaint()
            }
        }
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        if(!attached.get()) attach()
        WindowEmbed.INSTANCE.resizeWindow(osrsHandle, selfHandle!!)
    }

    interface WindowEmbed : Library {
        fun embedWindow(targetWindow: WinDef.HWND, parentWindow: WinDef.HWND)
        fun resizeWindow(targetWindow: WinDef.HWND, parentWindow: WinDef.HWND)

        companion object {
            val INSTANCE = Native.load("spectral", WindowEmbed::class.java)
        }
    }
}