package org.spectralpowered.util

import kotlin.concurrent.thread

inline fun every(delay: Long, crossinline action: () -> Unit) = thread {
    while (!Thread.interrupted()) {
        try {
            action()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Thread.sleep(delay)
    }
}