/*
 * Copyright (C) 2021 Spectral Powered <Kyle Escobar>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.spectralpowered.launcher.splashscreen

import javafx.scene.image.Image
import javafx.stage.Stage
import javafx.stage.StageStyle
import tornadofx.*

class SplashScreenApp : App(SplashScreenView::class) {

    init {
        ICONS.forEach { addStageIcon(it) }
        importStylesheet("/themes/spectral.css")
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.initStyle(StageStyle.UNDECORATED)
        super.start(stage)
        stage.requestFocus()
    }

    companion object {

        private val ICONS = listOf(
            "/images/icons/icon16.png",
            "/images/icons/icon32.png",
            "/images/icons/icon64.png",
            "/images/icons/icon128.png",
            "/images/icons/icon256.png",
            "/images/icons/icon512.png",
        ).map { Image(it) }
    }
}