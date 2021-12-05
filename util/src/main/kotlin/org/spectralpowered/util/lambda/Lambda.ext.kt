package org.spectralpowered.util.lambda

/**
 *
 * Created by Kyle Escobar on 11/14/16.
 */
@Deprecated("Use 'transform' instead.", ReplaceWith("transform(mapper)"))
inline fun <A, B> (() -> A).map(noinline mapper: (A) -> B): () -> B = transform(mapper)
inline fun <A, B> (() -> A).transform(noinline mapper: (A) -> B): () -> B {
    return { this.invoke().let(mapper) }
}