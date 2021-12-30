#include "NativeCanvas.h"

WNDPROC defWindowProc;

LRESULT EmbeddedWindowProc(HWND hwnd, UINT uMsg, WPARAM wparam, LPARAM lparam) {
    if(uMsg == WM_NCCALCSIZE) return false;
    else return CallWindowProc(defWindowProc, hwnd, uMsg, wparam, lparam);
}

void NativeCanvas::resizeWindow(HWND targetHwnd, HWND parentHwnd) {
    WINDOWINFO info;
    GetWindowInfo(parentHwnd, &info);

    int width = info.rcClient.right - info.rcClient.left;
    int height = info.rcClient.bottom - info.rcClient.top;

    MoveWindow(targetHwnd, 0, 0, width, height, false);

    /*
     * Update the window without passing the WM_NCCALCSIZE message to the parent window to
     * prevent the borders from re-appearing.
     */
    SetWindowLong(targetHwnd, GWL_STYLE, WS_VISIBLE);
    SetWindowPos(targetHwnd, nullptr, 0, 0, 0, 0, SWP_NOZORDER|SWP_NOOWNERZORDER|SWP_NOMOVE|SWP_NOSIZE|SWP_FRAMECHANGED);
}

void NativeCanvas::embedWindow(HWND targetHwnd, HWND parentHwnd) {
    SetWindowLong(targetHwnd, GWL_STYLE, WS_VISIBLE);
    SetParent(targetHwnd, parentHwnd);
    ShowWindow(targetHwnd, SW_SHOW);

    defWindowProc = (WNDPROC) GetWindowLongPtr(targetHwnd, GWLP_WNDPROC);
    SetWindowLongPtr(targetHwnd, GWLP_WNDPROC, (LONG_PTR)&EmbeddedWindowProc);
}
