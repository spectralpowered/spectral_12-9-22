#pragma once
#ifndef SPECTRAL_WINDOWEMBED_H
#define SPECTRAL_WINDOWEMBED_H

#include "../common.h"

namespace WindowEmbed {
    JVM_API void embedWindow(HWND targetWindow, HWND parentWindow);
    JVM_API void resizeWindow(HWND targetWindow, HWND parentWindow);
}

#endif //SPECTRAL_WINDOWEMBED_H
