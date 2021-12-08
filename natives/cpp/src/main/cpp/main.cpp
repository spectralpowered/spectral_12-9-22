#pragma comment(lib, "PolyHook_2.lib")

#include <windows.h>
#include <iostream>
#include <polyhook2/CapstoneDisassembler.hpp>

PLH::CapstoneDisassembler* disasm = nullptr;

void init()
{
    AllocConsole();
    FILE* f;
    freopen_s(&f, "CONOUT$", "w", stdout);
    freopen_s(&f, "CONOUT$", "w", stderr);

    disasm = new PLH::CapstoneDisassembler(PLH::Mode::x64);
}

BOOL WINAPI DllMain(HMODULE hModule, DWORD dwReason, LPVOID lpReserved)
{
    if(dwReason == DLL_PROCESS_ATTACH)
    {
        CreateThread(nullptr, 0, reinterpret_cast<LPTHREAD_START_ROUTINE>(init), nullptr, 0, nullptr);
    }
    return TRUE;
}