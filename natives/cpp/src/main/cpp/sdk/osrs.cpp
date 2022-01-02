#include "osrs.h"

namespace osrs {
    namespace module {
        void init() {
            handle = GetModuleHandleA("osclient.exe");
            address = reinterpret_cast<uint64_t>(handle);
        }
    }

    void init() {
        module::init();
    }
}