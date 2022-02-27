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

package org.spectralpowered.gradle.plugin.update4j

import org.gradle.api.Project
import java.io.File
import kotlin.collections.ArrayList


open class Update4jGradleExtension(project: Project) {
    fun resolveOuptutFile(): File {
        return File(_project.buildDir, output ?: DEFAULT_OUTPUT_FILE)
    }

    var file: String
        get() = _files[0].toString()
        set(descriptor) {
            val artifact: FileSpec = FileSpec.parse(descriptor)
            _files.add(artifact)
        }
    val artifactList: List<FileSpec>
        get() = _files

    fun setDefaultFolder(base_path: String) {
        artifactDefaultFolder = base_path
    }

    var folder: String
        get() = _folders[0].toString()
        set(descriptor) {
            val folder: FolderSpec = FolderSpec.parse(descriptor)
            _folders.add(folder)
        }
    val artifactFolderList: List<FolderSpec>
        get() = _folders
    private val _project: Project

    // current
    var uri: String? = null
    var path: String? = null
    var output: String? = null
    var artifactDefaultFolder: String? = null
        private set
    private val _files: MutableList<FileSpec> = ArrayList()
    private val _folders: MutableList<FolderSpec> = ArrayList()

    /**
     * Obsolete stuff
     */
    var outputLocation: String? = null
    var launcherFolder: String? = null
    var addedAssetLocation: String? = null
    private var _added_path: String? = null

    fun resolveAdditionalLocation(): File? {
        return if (addedAssetLocation != null) File(addedAssetLocation!!) else null
    }

    fun resolveOutputLocation(): File {
        return if (outputLocation != null) File(outputLocation!!) else File(_project.buildDir, "update4j")
    }

    fun resolveLauncherFolder(): File {
        return if (launcherFolder != null) File(launcherFolder!!) else File(
            _project.buildDir,
            "install/" + _project.name
        )
    }

    var addedAssetPath: String?
        get() = if (_added_path != null) _added_path else ""
        set(added_path) {
            _added_path = added_path
        }

    companion object {
        const val DEFAULT_OUTPUT_FILE = "update4j/config.xml"
    }

    init {
        _project = project
    }
}