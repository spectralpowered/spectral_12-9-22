#include "WindowEmbed.h"

WNDPROC defWindowProc;

LRESULT EmbeddedWindowProc(HWND hwnd, UINT uMsg, WPARAM wparam, LPARAM lparam) {
    if(uMsg == WM_NCCALCSIZE) return false;
    else {
        std::cout << "Msg: " << uMsg << std::endl;
        return CallWindowProc(defWindowProc, hwnd, uMsg, wparam, lparam);
    }
}

void WindowEmbed::embedWindow(HWND targetWindow, HWND parentWindow) {
    SetWindowLong(targetWindow, GWL_STYLE, WS_VISIBLE);
    SetParent(targetWindow, parentWindow);
    ShowWindow(targetWindow, SW_SHOW);

    defWindowProc = (WNDPROC) GetWindowLongPtr(targetWindow, GWLP_WNDPROC);
    SetWindowLongPtr(targetWindow, GWLP_WNDPROC, (LONG_PTR)&EmbeddedWindowProc);
}

void WindowEmbed::resizeWindow(HWND targetWindow, HWND parentWindow) {
    WINDOWINFO info;
    GetWindowInfo(parentWindow, &info);

    int width = info.rcClient.right - info.rcClient.left;
    int height = info.rcClient.bottom - info.rcClient.top;

    MoveWindow(targetWindow, 0, 0, width, height, false);

    /*
     * After we move the window, the WM_NCCALCSIZE is sent which will re-add the window borders.
     * If we just straight ignore it in our WndProc, the window will not update it's size.
     *
     * Solution: We update the window's style to WS_VISIBLE which is borderless, and then we use
     * SetWindowPos and include the SWP_NOMOVE and SWP_NOSIZE flags which will force the window
     * to recalculate without sending another WM_MOVE msg.
     */
    SetWindowLong(targetWindow, GWL_STYLE, WS_VISIBLE);
    SetWindowPos(targetWindow, nullptr, 0, 0, 0, 0, SWP_NOZORDER|SWP_NOOWNERZORDER|SWP_NOMOVE|SWP_NOSIZE|SWP_FRAMECHANGED);
}
