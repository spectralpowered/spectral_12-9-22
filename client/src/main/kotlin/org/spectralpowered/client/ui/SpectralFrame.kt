package org.spectralpowered.client.ui

import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinUser
import org.spectralpowered.client.ui.natives.NativeClientCanvas
import org.spectralpowered.common.inject
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Rectangle
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.UIManager

class SpectralFrame : JFrame("Spectral") {

    private val spectralUI: SpectralUI by inject()

    private lateinit var clientCanvas: NativeClientCanvas
    private val menuBar = MenuBar()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImages = SPECTRAL_ICONS
        layout = BorderLayout()
        this.initWindowSize()
        this.initMenuBar()
    }

    fun open() {
        setLocationRelativeTo(null)
        isVisible = true

        clientCanvas = NativeClientCanvas(spectralUI.osrsWindow)
        add(clientCanvas, BorderLayout.CENTER)
    }

    fun close() {
        isVisible = false
    }

    private fun initWindowSize() {
        val info = WinUser.WINDOWINFO()
        User32.INSTANCE.GetWindowInfo(spectralUI.osrsWindow, info)

        val width = info.rcClient.right - info.rcClient.left
        val height = info.rcClient.bottom - info.rcClient.top
        val x = info.rcClient.left
        val y = info.rcClient.top

        bounds = Rectangle(width, height, x, y)
        preferredSize = Dimension(width, height)
        size = preferredSize
        minimumSize = Dimension(800, 600)
    }

    private fun initMenuBar() {
        rootPane.putClientProperty("JRootPane.titleBarShowIcon", true)
        UIManager.put("TitlePane.showIcon", true)
        jMenuBar = menuBar
    }

    companion object {

        private val SPECTRAL_ICONS = listOf(
            "/images/icons/icon16.png",
            "/images/icons/icon32.png",
            "/images/icons/icon64.png",
            "/images/icons/icon128.png",
            "/images/icons/icon256.png",
            "/images/icons/icon512.png",
            "/images/icons/icon1024.png"
        ).map { ImageIcon(SpectralFrame::class.java.getResource(it)).image }
    }
}