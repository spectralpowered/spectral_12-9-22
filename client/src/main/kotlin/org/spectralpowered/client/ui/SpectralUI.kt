package org.spectralpowered.client.ui

import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import com.sun.jna.ptr.IntByReference
import org.spectralpowered.client.ui.theme.SpectralTheme
import org.spectralpowered.logger.Logger
import org.spectralpowered.util.retry
import java.lang.management.ManagementFactory
import javax.swing.JDialog
import javax.swing.JFrame

class SpectralUI {

    lateinit var frame: SpectralFrame private set

    /**
     * The window handle of the OSRS client.
     */
    lateinit var osrsWindow: WinDef.HWND private set

    private fun init() {
        /*
         * Install the Spectral Swing Look-And-Feel theme.
         */
        JFrame.setDefaultLookAndFeelDecorated(true)
        JDialog.setDefaultLookAndFeelDecorated(true)
        SpectralTheme.install()

        /*
         * Find the Old School RuneScape client window handle.
         */
        this.detectOSRSWindow()
    }

    fun open() {
        Logger.info("Opening Spectral UI.")

        this.init()
        frame = SpectralFrame()
        frame.open()
    }

    fun close() {
        Logger.info("Closing Spectral UI.")
        frame.close()
    }

    private fun detectOSRSWindow() {
        Logger.info("Searching Old School RuneScape client window.")

        val parentPID = ManagementFactory.getRuntimeMXBean().name.split("@").first().toInt()
        val startTime = System.currentTimeMillis()
        retry(0L) {
            if(System.currentTimeMillis() - startTime >= 30000) {
                Logger.error("Failed to detect Old School RuneScape client window.")
            }

            osrsWindow = WindowUtils.getAllWindows(true).first {
                val pid = IntByReference()
                User32.INSTANCE.GetWindowThreadProcessId(it.hwnd, pid)
                it.title == "Old School RuneScape" &&
                        pid.value == ManagementFactory.getRuntimeMXBean().name.split("@").first().toInt()
            }.hwnd
        }

        Logger.info("Found the Old School RuneScape client window with processID: $parentPID.")
    }
}