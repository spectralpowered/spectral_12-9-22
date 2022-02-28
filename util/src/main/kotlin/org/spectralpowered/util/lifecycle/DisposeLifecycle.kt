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

package org.spectralpowered.util.lifecycle

import org.spectralpowered.util.Disposable
import java.util.*

/**
 * Created by Kyle Escobar on 6/28/16.
 */
class DisposeLifecycle() : LifecycleConnectable, Disposable {
    val toRemove = ArrayList<LifecycleListener>()
    override fun connect(listener: LifecycleListener) {
        listener.onStart()
        toRemove.add(listener)
    }

    override fun dispose() {
        toRemove.forEach { it.onStop() }
        toRemove.clear()
    }

}