package org.spectralpowered.common

import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ObservableProperty<T>(default: T? = null) : ReadWriteProperty<Any, T> {

    private val observable = BehaviorSubject.create<T>().also {
        it.onNext(default)
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return observable.value as T
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        observable.onNext(value)
    }

    internal fun subscribe(action: T.() -> Unit) {
        observable.subscribe(action)
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Any> KProperty<T>.subscribe(action: T.() -> Unit) {
    (this::getter.getDelegate() as ObservableProperty<T>).subscribe(action)
}

@Suppress("UNCHECKED_CAST")
fun <T> T.subscribe(action: T.() -> Unit) = object : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        (property::getter.getDelegate() as ObservableProperty<T>).subscribe(action)
        return property::getter.get() as T
    }
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {}
}