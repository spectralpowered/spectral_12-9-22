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

import org.gradle.api.*;
import org.gradle.api.tasks.*;
import org.update4j.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

open class GenerateConfigurationFileTask : DefaultTask() {
    @TaskAction
    @Throws(IOException::class)
    fun create() {
        val extension = project.extensions.findByType(
            Update4jGradleExtension::class.java
        )

        val url = extension!!.uri
            ?: throw IllegalStateException("The 'uri' property must be provided in a 'update4j' block. This property must specify the base URL of the files to retrieve for the update.")

        val path = extension.path
            ?: throw IllegalStateException("The 'path' property must be provided in a 'update4j' block. This property must specify the base path of the files to update (in the installation).")

        var builder: Configuration.Builder = Configuration.builder().baseUri(URI(extension.uri!!)).basePath(path)
        builder = addFiles(extension, builder)
        builder = addFolders(extension, builder)
        val config: Configuration = builder.build()
        val file: File = extension.resolveOuptutFile()
        file.parentFile.mkdirs()
        val outstream = FileOutputStream(file)
        val printer = PrintWriter(outstream)
        writeExtra(printer)
        config.write(printer)
        printer.flush()
        outstream.flush()
        outstream.close()
    }

    private fun addFiles(extension: Update4jGradleExtension, builder: Configuration.Builder): Configuration.Builder {
        var builder: Configuration.Builder = builder
        val build_dir: File = project.buildDir
        var base_dir: File = build_dir
        if (extension!!.artifactDefaultFolder != null) base_dir =
            File(project.projectDir.resolve("src/resources/"), extension.artifactDefaultFolder)
        val base_path: Path = base_dir.toPath().normalize()
        for (spec in extension.artifactList) {
            var file = File(spec.name)
            if (!file.isAbsolute()) file = File(build_dir, spec.name)
            if (!file.exists() && File(base_dir, spec.name).exists()) file = File(base_dir, spec.name)
            require(file.exists()) { "The specified file does not exist: " + file.getAbsolutePath() }
            require(!file.isDirectory) { "The specified file is a folder: " + file.getAbsolutePath() }
            val artifact_path: Path = file.toPath()
            var relative_path: Path = base_path.relativize(artifact_path)
            if (spec.path != null) relative_path = Paths.get(spec.path)
            builder = builder.file(
                FileMetadata.readFrom(file.getAbsolutePath()).path(relative_path).classpath(spec.isClasspath)
                    .modulepath(spec.isModulepath).ignoreBootConflict(spec.isIgnoreBootConflict)
            )
        }
        return builder
    }

    private fun addFolders(extension: Update4jGradleExtension?, builder: Configuration.Builder): Configuration.Builder {
        var builder: Configuration.Builder = builder
        val build_dir: File = project.buildDir
        var base_dir: File = build_dir
        if (extension!!.artifactDefaultFolder != null) base_dir =
            File(project.buildDir, extension.artifactDefaultFolder)
        val base_path: Path = base_dir.toPath().normalize()
        for (spec in extension.artifactFolderList) {
            val folder = File(build_dir, spec.name)
            require(folder.exists()) { "The specified artifact folder does not exist: " + folder.getAbsolutePath() }
            require(folder.isDirectory()) { "The specified folder param does not specify a folder: " + folder.getAbsolutePath() }
            val files: Array<File> = folder.listFiles()
                ?: throw IllegalArgumentException("The specified folder is empty: " + folder.getAbsolutePath())
            var folder_base_path: Path = base_path
            if (spec.base != null) folder_base_path = File(build_dir, spec.base).toPath().normalize()
            //debug(String.format("folder_base_path=%s\n", folder_base_path));
            for (artifact_file in files) {
                if (!Files.isRegularFile(artifact_file.toPath())) continue
                val artifact_path: Path = artifact_file.toPath().normalize()
                var relative_path: Path = folder_base_path.relativize(artifact_path)
                if (spec.path != null) relative_path = Paths.get(spec.path + artifact_file.getName())
                //debug(String.format("relative_path=%s\n", relative_path));
                builder = builder.file(
                    FileMetadata.readFrom(artifact_file.getAbsolutePath()).path(relative_path)
                        .classpath(spec.isClasspath).modulepath(spec.isModulepath)
                        .ignoreBootConflict(spec.isIgnoreBootConflict)
                )
            }
        }
        return builder
    }

    /**
     * Useful for getting debug messages from the task.
     */
    private fun debug(message: String) {
        _extra_info.append(message)
        System.err.print(message)
    }

    private fun writeExtra(printer: PrintWriter) {
        val extra = _extra_info.toString()
        if (extra.length > 0) {
            printer.println("<!--")
            printer.println(extra)
            printer.println("-->")
        }
    }

    private val _extra_info = StringBuilder()
}