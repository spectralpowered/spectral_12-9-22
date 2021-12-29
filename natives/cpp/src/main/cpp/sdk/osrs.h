#pragma once
#ifndef SPECTRAL_OSRS_H
#define SPECTRAL_OSRS_H

#include "../common.h"

namespace osrs {
    namespace module {
        inline HANDLE handle;
        inline uint64_t address = 0;
        void init();
    }

    void init();
}


#endif //SPECTRAL_OSRS_H
