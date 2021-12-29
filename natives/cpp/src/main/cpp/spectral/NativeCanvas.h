#pragma once
#ifndef SPECTRAL_NATIVECANVAS_H
#define SPECTRAL_NATIVECANVAS_H

#include "../common.h"

namespace NativeCanvas {
    JVM_EXPORT void resizeWindow(HWND targetHwnd, HWND parentHwnd);
    JVM_EXPORT void embedWindow(HWND targetHwnd, HWND parentHwnd);
}

#endif //SPECTRAL_NATIVECANVAS_H
