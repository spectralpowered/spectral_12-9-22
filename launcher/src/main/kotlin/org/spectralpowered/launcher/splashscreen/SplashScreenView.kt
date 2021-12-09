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
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.image.Image
import javafx.scene.text.Font
import org.spectralpowered.launcher.Launcher
import org.spectralpowered.logger.Logger
import tornadofx.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class SplashScreenView : View("Spectral") {

    val progress = SimpleDoubleProperty(0.1)
    val progressText = SimpleStringProperty("Initializing.")

    override val root = vbox {
        style = "-fx-border-color: derive(-fx-background, -25%);" +
                "-fx-border-width: 3px;" +
                "-fx-border-style: solid;"
        setPrefSize(425.0, 375.0)
        alignment = Pos.CENTER

        imageview(Image("/images/spectral-transparent.png")) {
            fitWidth = 96.0
            fitHeight = 96.0
        }

        label("S P E C T R A L") {
            paddingTop = 32.0
            font = Font(32.0)
            paddingBottom = 40.0
        }

        progressbar(progress) {
            prefWidth = 300.0
        }

        label(progressText) {
            paddingTop = 16.0
            font = Font(14.0)
        }
    }

    override fun onDock() {
        thread {
            try {
                Launcher.launch()
                Platform.runLater { close() }
            } catch (e : Exception) {
                Logger.error(e) { "An error occurred while launching the Spectral client." }
                exitProcess(-1)
            }
        }
    }
}