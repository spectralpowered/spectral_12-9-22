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

package org.spectralpowered.plugin.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.attributes
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.project

open class SpectralGradlePlugin : Plugin<Project>  {

    override fun apply(project: Project) {
        val cfg = project.extensions.create("plugin", SpectralGradlePluginExtension::class.java)

        project.tasks.named<Jar>("jar") {
            manifest {
                attributes("Plugin-Id" to cfg.id)
                attributes("Plugin-Class" to cfg.mainClass)
                attributes("Plugin-Version" to cfg.version)
                attributes("Plugin-Provider" to cfg.author)
            }
        }
    }
}