#include <windows.h>
#include <iostream>
#include "util/jni/jnipp.h"

using namespace std;

void initConsole()
{
    AllocConsole();
    FILE* f;
    freopen_s(&f, "CONOUT$", "w", stdout);
    freopen_s(&f, "CONOUT$", "w", stderr);
}

void initSpectralJVM() {
    jni::Vm createSpectralJvm;
    jni::Class cls = jni::Class("org/spectralpowered/launcher/Launcher");
    jni::method_t method = cls.getStaticMethod("start", "()V");
    cls.call<void>(method);
}

void init()
{
    initConsole();
    initSpectralJVM();
}

BOOL WINAPI DllMain(HMODULE hModule, DWORD dwReason, LPVOID lpReserved)
{
    if(dwReason == DLL_PROCESS_ATTACH)
    {
        CreateThread(nullptr, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(init), nullptr, 0, nullptr);
    }
    return TRUE;
}