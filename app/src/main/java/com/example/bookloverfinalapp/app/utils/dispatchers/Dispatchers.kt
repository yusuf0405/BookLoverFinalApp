package com.example.bookloverfinalapp.app.utils.dispatchers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Dispatchers {

    fun launchUi(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job

    fun launchInBackground(scope: CoroutineScope, block: suspend CoroutineScope.() -> Unit): Job

    suspend fun changeToUi(block: suspend CoroutineScope.() -> Unit)

    suspend fun changeToBackground(block: suspend CoroutineScope.() -> Unit)

    class Base : Dispatchers {
        override fun launchUi(
            scope: CoroutineScope,
            block: suspend CoroutineScope.() -> Unit,
        ): Job =
            scope.launch(kotlinx.coroutines.Dispatchers.Main, block = block)


        override fun launchInBackground(
            scope: CoroutineScope,
            block: suspend CoroutineScope.() -> Unit,
        ): Job =
            scope.launch(kotlinx.coroutines.Dispatchers.IO, block = block)

        override suspend fun changeToUi(block: suspend CoroutineScope.() -> Unit) =
            withContext(kotlinx.coroutines.Dispatchers.Main, block)

        override suspend fun changeToBackground(block: suspend CoroutineScope.() -> Unit) =
            withContext(kotlinx.coroutines.Dispatchers.IO, block)

    }
}