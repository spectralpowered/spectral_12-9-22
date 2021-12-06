package org.spectralpowered.client.ui.theme

import com.formdev.flatlaf.IntelliJTheme

class SpectralTheme : IntelliJTheme.ThemeLaf(IntelliJTheme(
    SpectralTheme::class.java.getResourceAsStream("/themes/spectral.theme.json")
)) {

    override fun getName(): String = "Spectral"

    companion object {
        fun install() = setup(SpectralTheme())
    }
}