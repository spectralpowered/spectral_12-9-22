package org.spectralpowered.launcher

import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.*
import com.sun.jna.ptr.IntByReference
import org.spectralpowered.common.SpectralPaths
import org.spectralpowered.common.SpectralUrls
import org.spectralpowered.logger.Logger
import org.spectralpowered.util.retry
import java.awt.Desktop
import java.io.File
import java.io.FileOutputStream
import java.net.URI
import java.net.URL
import java.nio.ByteBuffer
import java.util.zip.ZipFile
import kotlin.system.exitProcess

object Launcher {

    private const val OSRS_STEAM_APPID = 1343370
    private const val OSRS_STEAM_WINDOW_TITLE = "Old School RuneScape"
    private const val OSRS_PROCESS_NAME = "osclient.exe"

    private var processId: Int = -1

    @JvmStatic
    fun main(args: Array<String>) {
        this.init()
        this.launch()
    }

    private fun init() {
        Logger.info("Initializing...")

        this.checkDirs()
        this.downloadAdoptJDK()
        BinaryFileUpdater.run()
    }

    private fun launch() {
        Logger.info("Launching Spectral...")

        this.startSteamClient()
        this.injectSpectralDLLs()

        /*
         * Spectral launcher complete. Exit the launcher JVM process as the injected spectral.dll
         * will have launched the Spectral client's JVM from inside the Old School RuneScape Steam client's
         * process.
         */
        exitProcess(0)
    }

    private fun checkDirs() {
        Logger.info("Checking required Spectral directories.")
        SpectralPaths.allDirs().forEach { dir ->
            if(!dir.exists()) {
                Logger.info("Creating required directory: ${dir.path}")
                dir.mkdirs()
            }
        }
    }

    private fun downloadAdoptJDK() {
        if(SpectralPaths.jreDir.listFiles()!!.isEmpty()) {
            Logger.info("Downloading AdoptJDK 16 for Spectral...")

            val bytes = URL(SpectralUrls.ADOPT_JDK_DOWNLOAD_URL).openConnection().getInputStream().readAllBytes()
            FileOutputStream(SpectralPaths.jreDir.resolve("adoptjdk16.zip")).use {
                it.write(bytes)
            }

            Logger.info("Extracting AdoptJDK 16 archive...")
            ZipFile(SpectralPaths.jreDir.resolve("adoptjdk16.zip")).use { zip ->
                zip.entries().asSequence().forEach entryLoop@ { entry ->
                    val name = entry.name.replace("jdk-16.0.1+9-jre/", "")
                    if(entry.isDirectory) {
                        if(name == "jdk-16.0.1+9-jre" || name == "") return@entryLoop
                        SpectralPaths.jreDir.resolve(name).mkdirs()
                    } else {
                        zip.getInputStream(entry).use { input ->
                            SpectralPaths.jreDir.resolve(name).outputStream().use { output ->
                                input.copyTo(output)
                            }
                        }
                    }
                }
            }

            SpectralPaths.jreDir.resolve("adoptjdk16.zip").deleteRecursively()

            Logger.info("Successfully downloaded and extracted AdoptJDK 16 files.")
        }
    }

    private fun startSteamClient() {
        Logger.info("Starting Old School RuneScape Steam client.")

        val protocol = URI("steam://run/$OSRS_STEAM_APPID")
        Desktop.getDesktop().browse(protocol)

        /*
         * Wait until we detect the Steam client process has started.
         * Timeout for launch is 30 seconds.
         */

        val startTime = System.currentTimeMillis()
        retry(128L) {
            if(System.currentTimeMillis() - startTime >= 30000) {
                Logger.error("Failed to launch the Old School RuneScape steam client within the 30 second timeout.")
                exitProcess(0)
            }

            WindowUtils.getAllWindows(true).forEach {
                if(it.title == "Old School RuneScape") {
                    val pid = IntByReference()
                    User32.INSTANCE.GetWindowThreadProcessId(it.hwnd, pid)
                    this.processId = pid.value
                }
            }

            /*
             * Throw an exception so that the retry block will try again if the processId
             * is still set to -1.
             */
            if(this.processId == -1) {
                throw Exception()
            }
        }

        Logger.info("Found Old School RuneScape client running processID: $processId.")
    }

    private fun injectSpectralDLLs() {
        Logger.info("Injecting Spectral client DLL files.")

        /*
         * Inject the required dependency DLLs first and then the spectral.dll into
         * the Old School RuneScape Steam client process.
         */
        injectDLL(SpectralPaths.spectralDll)

        Logger.info("Successfully injected Spectral client DLLs into Old School RuneScape Steam client process.")
    }

    private fun injectDLL(file: File) {
        Logger.info("Injecting DLL (${file.path})...")

        val dllPath = file.absolutePath.toString() + "\u0000"
        val hProcess = Kernel32.INSTANCE.OpenProcess(WinNT.PROCESS_ALL_ACCESS, false, processId)
        val pDllPath = Kernel32.INSTANCE.VirtualAllocEx(hProcess, null, BaseTSD.SIZE_T(dllPath.length.toLong()), WinNT.MEM_COMMIT or WinNT.MEM_RESERVE,
            WinNT.PAGE_EXECUTE_READWRITE
        )

        val buf = ByteBuffer.allocateDirect(dllPath.length)
        buf.put(dllPath.toByteArray())
        val bufPtr = Native.getDirectBufferPointer(buf)
        val bytesWritten = IntByReference()

        Kernel32.INSTANCE.WriteProcessMemory(hProcess, pDllPath, bufPtr, dllPath.length, bytesWritten)
        if(dllPath.length != bytesWritten.value) {
            throw RuntimeException("Failed to inject the DLL into the process. Invalid number of bytes were written to memory.")
        }

        val kernel32 = NativeLibrary.getInstance("kernel32")
        val loadLibraryA = kernel32.getFunction("LoadLibraryA")

        val threadId = WinDef.DWORDByReference()
        val hThread = Kernel32.INSTANCE.CreateRemoteThread(hProcess, null, 0, loadLibraryA, pDllPath, 0, threadId)
        Kernel32.INSTANCE.WaitForSingleObject(hThread, WinNT.INFINITE)
        Kernel32.INSTANCE.VirtualFreeEx(hProcess, pDllPath, BaseTSD.SIZE_T(0), WinNT.MEM_RELEASE)

        Logger.info("Successfully injected DLL into process memory.")
    }
}