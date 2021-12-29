#include "osrs.h"

namespace osrs {
    namespace module {
        void init() {
            handle = GetModuleHandleA("osclient.exe");
            address = reinterpret_cast<uint64_t>(handle);
            std::cout << "Base Address: " << std::to_string(address) << std::endl;
        }
    }

    void init() {
        std::cout << "Initializing Spectral natives SDK." << std::endl;
        module::init();
    }
}