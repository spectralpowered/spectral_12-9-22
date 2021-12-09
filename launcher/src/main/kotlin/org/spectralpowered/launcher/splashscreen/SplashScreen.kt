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

import javafx.application.Platform
import org.spectralpowered.logger.Logger
import tornadofx.FX
import tornadofx.launch

object SplashScreen {

    private val view by lazy { FX.find<SplashScreenView>() }

    fun open() {
        Logger.info("Opening Spectral splashscreen.")
        launch<SplashScreenApp>()
    }

    var progress: Double
        get() = view.progress.get()
        set(value) = Platform.runLater { view.progress.set(value) }

    var progressText: String
        get() = view.progressText.get()
        set(value) = Platform.runLater { view.progressText.set(value) }
}