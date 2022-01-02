package org.spectralpowered.api

import org.spectralpowered.common.inject

interface Console {

    /**
     * Sends / adds a basic message to the Old School RuneScape
     * debug console.
     * @param message String
     */
    fun message(message: String)

    /**
     * Logs a simple and plain message to the Old School RuneScape debug console.
     * @param message String
     */
    fun log(message: String) = message(message)

    /**
     * Logs a debugging message to the Old School RuneScape debug console.
     * @param message String
     */
    fun debug(message: String) = message("<col=00FFFF>[DEBUG]</col> $message")

    /**
     * Logs an informational message to the Old School RuneScape debug console.
     * @param message String
     */
    fun info(message: String) = message("<col=00FF00>[INFO]</col> $message")

    /**
     * Logs a warning message to the Old School RuneScape debug console.
     * @param message String
     */
    fun warn(message: String) = message("<col=FFCC00>[WARN]</col> $message")

    /**
     * Logs an error or critical message to the Old School RuneScape debug console.
     * @param message String
     */
    fun error(message: String) = message("<col=FF0000>[ERROR]</col> $message")
    
    companion object : Console by Spectral.console
}