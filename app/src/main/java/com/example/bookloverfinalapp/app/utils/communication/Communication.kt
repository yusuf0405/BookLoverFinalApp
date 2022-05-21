package com.example.bookloverfinalapp.app.utils.communication

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

interface Communication<T> : Observe<T>, Mapper.Void<T> {

    abstract class Base<T : Any> : Communication<T> {
        private val _observer = MutableSharedFlow<T>(replay = 1,
            extraBufferCapacity = 0,
            onBufferOverflow = BufferOverflow.DROP_OLDEST)
        private var liveData = MutableLiveData<T>()

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }

        override fun observe(scope: CoroutineScope, observer: Observer<T>) {
            _observer.onEach {
                observer.onChanged(it)
            }.launchIn(scope = scope)
        }

        override fun put(source: T) = liveData.postValue(source)
    }
}


interface Observe<T> {
    fun observe(owner: LifecycleOwner, observer: Observer<T>)

    fun observe(scope: CoroutineScope, observer: Observer<T>)
}

interface Mapper<R, S> {

    fun put(source: S): R

    interface Void<T> : Mapper<Unit, T>

}