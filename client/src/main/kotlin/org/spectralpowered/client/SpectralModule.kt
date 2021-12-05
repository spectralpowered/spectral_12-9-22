package org.spectralpowered.client

import org.koin.dsl.module

val SPECTRAL_MODULE = module {
    single { Spectral() }
}