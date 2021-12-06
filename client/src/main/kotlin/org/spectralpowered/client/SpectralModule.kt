package org.spectralpowered.client

import org.koin.dsl.module
import org.spectralpowered.client.ui.SpectralUI

val SPECTRAL_MODULE = module {
    single { Spectral() }
    single { SpectralUI() }
}