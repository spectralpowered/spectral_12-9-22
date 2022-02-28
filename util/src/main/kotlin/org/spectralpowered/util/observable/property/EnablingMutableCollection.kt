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

package org.spectralpowered.util.observable.property

import java.util.*

/**
 * Created by Kyle Escobar on 12/2/16.
 */
abstract class EnablingMutableCollection<E>() : ArrayList<E>() {

    abstract fun enable(): Unit
    abstract fun disable(): Unit

    var active = false
    fun checkUp() {
        if (!super.isEmpty() && !active) {
            active = true
            enable()
        }
    }

    fun checkDown() {
        if (super.isEmpty() && active) {
            active = false
            disable()
        }
    }

    override fun add(element: E): Boolean {
        val result = super.add(element)
        checkUp()
        return result
    }

    override fun addAll(elements: Collection<E>): Boolean {
        val result = super.addAll(elements)
        checkUp()
        return result
    }

    override fun clear() {
        super.clear()
        checkDown()
    }

    override fun remove(element: E): Boolean {
        val result = super.remove(element)
        checkDown()
        return result
    }

    override fun removeAll(elements: Collection<E>): Boolean {
        val result = super.removeAll(elements)
        checkDown()
        return result
    }

    override fun retainAll(elements: Collection<E>): Boolean {
        val result = super.retainAll(elements)
        checkDown()
        return result
    }
}