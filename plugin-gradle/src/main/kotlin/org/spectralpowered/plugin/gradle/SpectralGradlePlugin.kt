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
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.support.zipTo
import java.io.File

open class SpectralGradlePlugin : Plugin<Project>  {

    private val pluginsDir = File(System.getProperty("user.home")).resolve(".spectral/plugins/")

    override fun apply(project: Project) {
        val cfg = project.extensions.create("plugin", SpectralGradlePluginExtension::class.java)

        project.group = cfg.id
        project.version = cfg.version

        project.tasks.named<Jar>("jar") {
            archiveBaseName.set("${cfg.id}-plugin")
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
            manifest {
                attributes("Plugin-Id" to cfg.id)
                attributes("Plugin-Class" to cfg.mainClass)
                attributes("Plugin-Version" to cfg.version)
                attributes("Plugin-Provider" to cfg.author)
            }
        }

        project.tasks.register<Copy>("assemblePlugin") {
            from(project.tasks.named("jar"))
            into(pluginsDir)
        }

        project.tasks.named("build") {
            dependsOn(project.tasks.named("jar"))
        }
    }
}