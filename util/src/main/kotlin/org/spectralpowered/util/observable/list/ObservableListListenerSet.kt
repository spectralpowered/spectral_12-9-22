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

/**
 * Created by Kyle Escobar on 5/5/16.
 */

class ObservableListListenerSet<T>(
        val onAddListener: (item: T, position: Int) -> Unit,
        val onRemoveListener: (item: T, position: Int) -> Unit,
        val onChangeListener: (old: T, item: T, position: Int) -> Unit,
        val onMoveListener: (item: T, oldPosition: Int, position: Int) -> Unit,
        val onReplaceListener: (list: ObservableList<T>) -> Unit
) {
}

inline fun <T> ObservableList<T>.addListenerSet(set: ObservableListListenerSet<T>) {
    onAdd.add(set.onAddListener)
    onRemove.add(set.onRemoveListener)
    onChange.add(set.onChangeListener)
    onMove.add(set.onMoveListener)
    onReplace.add(set.onReplaceListener)
}

inline fun <T> ObservableList<T>.removeListenerSet(set: ObservableListListenerSet<T>) {
    onAdd.remove(set.onAddListener)
    onRemove.remove(set.onRemoveListener)
    onChange.remove(set.onChangeListener)
    onMove.remove(set.onMoveListener)
    onReplace.remove(set.onReplaceListener)
}