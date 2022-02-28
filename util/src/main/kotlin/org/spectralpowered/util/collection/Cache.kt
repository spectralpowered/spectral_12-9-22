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

package org.spectralpowered.util.collection

import java.util.*

/**
 * Created by Kyle Escobar on 2/12/2016.
 */
open class Cache<K, V>(val map: MutableMap<K, V> = HashMap(), val generate: (K) -> V?) : MutableMap<K, V> by map {
    override fun get(key: K): V? {
        return map[key] ?: generate(key).apply {
            if (this != null)
                map[key] = this
        }
    }
}