#include <windows.h>
#include <iostream>
#include <ShlObj.h>
#include "util/jni/jnipp.h"

void openConsole() {
    AllocConsole();
    FILE* f;
    freopen_s(&f, "CONOUT$", "w", stdout);
    freopen_s(&f, "CONOUT$", "w", stderr);
}

void launchSpectralJVM() {
    /*
     * Find the Spectral base directory.
     */
    char path[MAX_PATH];
    SHGetFolderPath(nullptr, CSIDL_PROFILE, nullptr, 0, path);
    std::string spectralDir = std::string(path) + std::string("\\Spectral\\");

    /*
     * Launch the Spectral JVM using the Spectral client jar as the classpath.
     */
    auto jvmDllPath = spectralDir + std::string(R"(jre\bin\server\jvm.dll)");
    auto spectralJarPath = spectralDir + std::string("bin\\spectral.jar");

    jni::Vm createSpectralJVM(jvmDllPath.c_str(), spectralJarPath.c_str());
    jni::Class cls = jni::Class("org/spectralpowered/client/Spectral");
    jni::method_t method = cls.getStaticMethod("launch", "()V");
    cls.call<void>(method);
}

void init() {
    /*
     * Open the debugging console.
     * Comment this line out if not running in a development environment, or
     * you do not want the console window to appear.
     */
    openConsole();

    /*
     * Launch the Spectral client JVM by using JNI to create a Java Virtual machine
     * process from within the current process (Steam client).
     */
    launchSpectralJVM();
}

BOOL WINAPI DllMain(HMODULE hModule, DWORD dwReason, LPVOID lpReserved) {
    if(dwReason == DLL_PROCESS_ATTACH) {
        CreateThread(nullptr, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(init), nullptr, 0, nullptr);
    }
    return TRUE;
}
