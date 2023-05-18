package com.example.bookloverfinalapp.app.workers

import android.app.Application
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.ListenableWorker.Result
import androidx.work.WorkerParameters
import androidx.work.impl.WorkDatabase
import androidx.work.impl.foreground.ForegroundProcessor
import androidx.work.impl.utils.WorkForegroundUpdater
import androidx.work.impl.utils.WorkProgressUpdater
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor
import androidx.work.testing.SynchronousExecutor
import com.example.domain.use_cases.ClearAllAppCacheUseCase
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import java.util.*

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class ClearCacheWorkerTest {

    private lateinit var applicationContext: Application
    private val clearAllAppCacheUseCase = Mockito.mock(ClearAllAppCacheUseCase::class.java)

    @get:Rule
    var wmRule = WorkManagerTestRule()

    private val configuration = Configuration.Builder()
        .setMinimumLoggingLevel(Log.DEBUG)
        .setExecutor(SynchronousExecutor())
        .build()

    private val executor = configuration.executor
    private val workManagerTaskExecutor = WorkManagerTaskExecutor(executor)
    private lateinit var workDatabase: WorkDatabase
    private lateinit var workParameters: WorkerParameters
    private lateinit var worker: ClearCacheWorker

    @Before
    fun initTest() {
        applicationContext = ApplicationProvider.getApplicationContext()
        workDatabase = WorkDatabase.create(
            applicationContext, workManagerTaskExecutor.mainThreadExecutor,
            true
        )

        worker = ClearCacheWorker(
            context = applicationContext,
            workerParams = workParameters,
            clearAllAppCacheUseCase = clearAllAppCacheUseCase,
        )
    }

    @Test
    fun testClearCacheWorkerSuccess() = runBlocking {
        val result = worker.doWork()
        Mockito.verify(clearAllAppCacheUseCase).invoke()
        assertThat(result, `is`(Result.success()))
    }
}