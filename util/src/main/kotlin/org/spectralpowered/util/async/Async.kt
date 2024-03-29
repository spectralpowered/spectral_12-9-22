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

package org.spectralpowered.util.async

import java.util.*
import java.util.concurrent.*

/**
 * Various functions to do things asynchronously.
 * Created by Kyle Escobar on 9/2/15.
 */

object Async {
    val NUMBER_OF_CORES: Int = Runtime.getRuntime().availableProcessors()
    val KEEP_ALIVE_TIME: Long = 1
    val KEEP_ALIVE_TIME_UNIT: TimeUnit = TimeUnit.SECONDS
    fun generateThreadPool(queue: LinkedBlockingQueue<Runnable>) = ThreadPoolExecutor(
            NUMBER_OF_CORES, // Initial pool size
            NUMBER_OF_CORES, // Max pool size
            KEEP_ALIVE_TIME,
            KEEP_ALIVE_TIME_UNIT,
            queue
    )

    val runnableQueue = LinkedBlockingQueue<Runnable>()
    val threadPool = generateThreadPool(runnableQueue)

    var uiThreadInterface: AsyncInterface = object : AsyncInterface {
        override fun sendToThread(action: () -> Unit) = doAsync(action)
    }
}

inline fun <T> (() -> T).invokeAsync() {
    Async.threadPool.execute({
        invoke()
    })
}

inline fun <T> (() -> T).invokeAsyncFuture(): Future<T> = Async.threadPool.submit(this)

inline fun (() -> Unit).invokeUIThread() {
    Async.uiThreadInterface.sendToThread(this)
}

@JvmName("invokeUIThreadT")
inline fun <T> (() -> T).invokeUIThread() {
    Async.uiThreadInterface.sendToThread { invoke() }
}

inline fun <T> (() -> T).invokeAsync(crossinline callback: (T) -> Unit) {
    Async.threadPool.execute({
        val result = invoke()
        Async.uiThreadInterface.sendToThread { callback.invoke(result) }
    })
}

/**
 * Runs [action] asynchronously.
 * @param action The lambda to run asynchronously.
 */
fun <T> doAsync(action: () -> T) {
    Async.threadPool.execute({
        val result = action()
    })
}

/**
 * Runs [action] asynchronously with its result being dealt with on the UI thread in [uiThread].
 * @param action The lambda to run asynchronously.
 * @param uiThread The lambda to run with the result of [action] on the UI thread.
 */
fun <T> doAsync(action: () -> T, uiThread: (T) -> Unit) {
    Async.threadPool.execute({
        try {
            val result = action()
            Async.uiThreadInterface.sendToThread {
                uiThread(result)
            }
        } catch(e: Exception) {
            Async.uiThreadInterface.sendToThread {
                throw e
            }
        }
    })
}

/**
 * Posts [action] to the main thread.
 * @param action The lambda to run asynchronously.
 */
fun doUiThread(action: () -> Unit) {
    Async.uiThreadInterface.sendToThread(action)
}

fun <A, B> parallel(a: () -> A, b: () -> B): () -> Pair<A, B> {
    return {
        val futureA = FutureTask {
            a.invoke()
        }
        val futureB = FutureTask {
            b.invoke()
        }
        Thread(futureA).start()
        Thread(futureB).start()
        futureA.get() to futureB.get()
    }
}

fun <A, B, C> parallel(a: () -> A, b: () -> B, c: () -> C): () -> Triple<A, B, C> {
    return {
        val futureA = FutureTask {
            a.invoke()
        }
        val futureB = FutureTask {
            b.invoke()
        }
        val futureC = FutureTask {
            c.invoke()
        }
        Thread(futureA).start()
        Thread(futureB).start()
        Thread(futureC).start()
        Triple(futureA.get(), futureB.get(), futureC.get())
    }
}

fun <T> parallelNonblocking(tasks: List<() -> T>, onAllComplete: (List<T>) -> Unit) {
    if (tasks.isEmpty()) onAllComplete(listOf())
    val items = ArrayList<T>()
    for (task in tasks) {
        task.invokeAsync {
            items.add(it)
            if (items.size == tasks.size) {
                onAllComplete(items)
            }
        }
    }
}

@JvmName("parallelBlockingShorthand")
fun <T> List<() -> T>.parallel(): () -> List<T> = parallel(this)

fun <T> parallel(tasks: List<() -> T>): () -> List<T> {
    if (tasks.isEmpty()) return { listOf() }
    else if (tasks.size < Async.NUMBER_OF_CORES) {
        return {
            try {
                val results = tasks.subList(0, tasks.size - 1).map {
                    val future = FutureTask {
                        it.invoke()
                    }
                    Thread(future) to future
                }.map { it.first.start(); it.second }.map { it.get() }.toMutableList()
                results += tasks.last().invoke()
                results
            } catch(e: Exception) {
                e.printStackTrace()
                tasks.map { it() }
            }
        }
    } else {
        return {
            val queue = LinkedBlockingQueue<Runnable>()
            val pool = Async.generateThreadPool(queue)
            try {
                val results = tasks.subList(0, tasks.size - 1).map {
                    pool.submit(it)
                }.map { it.get() }.toMutableList()
                results += tasks.last().invoke()
                results
            } catch(e: Exception) {
                e.printStackTrace()
                tasks.map { it() }
            }
        }
    }
}


//Weird async stuff below

inline fun <T> List<T>.withEachAsync(doTask: T.(() -> Unit) -> Unit, crossinline onAllComplete: () -> Unit) {
    if (isEmpty()) {
        onAllComplete()
        return
    }
    var itemsToGo = size
    for (item in this) {
        item.doTask {
            itemsToGo--
            if (itemsToGo <= 0) {
                onAllComplete()
            }
        }
    }
}

inline fun <T, MUTABLE, RESULT> List<T>.withReduceAsync(
        doTask: T.((RESULT) -> Unit) -> Unit,
        initialValue: MUTABLE,
        crossinline combine: MUTABLE.(RESULT) -> Unit,
        crossinline onAllComplete: (MUTABLE) -> Unit
) {
    if (isEmpty()) {
        onAllComplete(initialValue)
        return
    }
    var total = initialValue
    var itemsToGo = size
    for (item in this) {
        item.doTask {
            combine(total, it)
            itemsToGo--
            if (itemsToGo <= 0) {
                onAllComplete(total)
            }
        }
    }
}

inline fun parallelAsyncs(tasks: Collection<(() -> Unit) -> Unit>, crossinline onComplete: () -> Unit) {
    if (tasks.isEmpty()) {
        onComplete()
        return
    }
    var itemsToGo = tasks.size
    for (item in tasks) {
        item {
            itemsToGo--
            if (itemsToGo <= 0) {
                onComplete()
            }
        }
    }
}

inline fun parallelAsyncs(vararg tasks: (() -> Unit) -> Unit, crossinline onComplete: () -> Unit) {
    if (tasks.isEmpty()) {
        onComplete()
        return
    }
    var itemsToGo = tasks.size
    for (item in tasks) {
        item {
            itemsToGo--
            if (itemsToGo <= 0) {
                onComplete()
            }
        }
    }
}