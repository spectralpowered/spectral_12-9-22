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

package org.spectralpowered.launcher.util

import com.sun.jna.Native
import com.sun.jna.NativeLibrary
import com.sun.jna.platform.win32.*
import com.sun.jna.platform.win32.Tlhelp32.TH32CS_SNAPPROCESS
import com.sun.jna.ptr.IntByReference
import org.spectralpowered.launcher.splashscreen.SplashScreen
import org.spectralpowered.logger.Logger
import java.io.File
import java.nio.ByteBuffer

object Injector {

    fun getProcessId(processName: String): Int {
        val snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(TH32CS_SNAPPROCESS, WinDef.DWORD(0))
        val procEntry = Tlhelp32.PROCESSENTRY32()

        if(!Kernel32.INSTANCE.Process32First(snapshot, procEntry)) {
            return -1
        }

        while(Kernel32.INSTANCE.Process32Next(snapshot, procEntry)) {
            if(Native.toString(procEntry.szExeFile) == processName) {
                Kernel32.INSTANCE.CloseHandle(snapshot)
                return procEntry.th32ProcessID.toInt()
            }
        }

        Kernel32.INSTANCE.CloseHandle(snapshot)
        return -1
    }

    fun injectDLL(processName: String, dllFile: File) = injectDLL(getProcessId(processName), dllFile)

    fun injectDLL(processId: Int, dllFile: File) {
        Logger.info("Injecting DLL (${dllFile.path})...")
        SplashScreen.progressText = "Injecting ${dllFile.name} into client."

        val dllPath = dllFile.absolutePath.toString() + "\u0000"
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