package com.example.bookloverfinalapp.app.utils.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {

    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun default(): CoroutineDispatcher

    fun unconfined(): CoroutineDispatcher

    class Base : DispatchersProvider {

        override fun main(): CoroutineDispatcher = Dispatchers.Main

        override fun io(): CoroutineDispatcher = Dispatchers.IO

        override fun default(): CoroutineDispatcher = Dispatchers.Default

        override fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
    }
}