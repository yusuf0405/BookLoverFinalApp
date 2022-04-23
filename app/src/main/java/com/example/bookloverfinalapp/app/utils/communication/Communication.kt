package com.example.bookloverfinalapp.app.utils.communication

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication<T> : Observe<T>, Mapper.Void<T> {

    abstract class Base<T> : Communication<T> {

        private var liveData = MutableLiveData<T>()

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }

        override fun map(source: T) = liveData.postValue(source!!)
    }
}

interface Observe<T> {

    fun observe(owner: LifecycleOwner, observer: Observer<T>)

}

interface Mapper<R, S> {

    fun map(source: S): R

    interface Void<T> : Mapper<Unit, T>

}