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

package org.spectralpowered.util.observable.list

import org.spectralpowered.util.lifecycle.LifecycleConnectable
import org.spectralpowered.util.lifecycle.LifecycleListener
import org.spectralpowered.util.observable.property.bind

/**
 * Extensions that allow using ObservablePropertys with the LifecycleConnectable.
 * Created by Kyle Escobar on 6/1/16.
 */

fun <T> LifecycleConnectable.bind(observable: ObservableList<T>, listener: (ObservableList<T>) -> Unit) {
    bind(observable.onUpdate, listener)
}

fun <T> LifecycleConnectable.bind(observable: ObservableList<T>, listenerSet: ObservableListListenerSet<T>) {
    connect(object : LifecycleListener {
        override fun onStart() {
            observable.addListenerSet(listenerSet)
        }

        override fun onStop() {
            observable.removeListenerSet(listenerSet)
        }
    })
}