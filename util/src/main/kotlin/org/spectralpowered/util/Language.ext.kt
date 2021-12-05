package org.spectralpowered.util

/**
 * Created by Kyle Escobar on 2/15/2017.
 */
inline fun <T> T.aside(action:(T)->Unit):T{
    action(this)
    return this
}