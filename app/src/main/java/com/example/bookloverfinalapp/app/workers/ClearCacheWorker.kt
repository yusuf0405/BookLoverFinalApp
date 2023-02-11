package com.example.bookloverfinalapp.app.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.example.domain.use_cases.ClearAllAppCacheUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

@HiltWorker
class ClearCacheWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val clearAllAppCacheUseCase: ClearAllAppCacheUseCase,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            Log.i("Joseph,","doWork")
            clearAllAppCacheUseCase()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.i("Joseph,",e.toString())
            return if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
        return Result.success()
    }

    companion object {
        private const val repeatInterval: Long = 2
        private val repeatIntervalTimeUnit: TimeUnit = TimeUnit.HOURS
        val TAG: String = ClearCacheWorker::class.java.name

        @JvmStatic
        fun getSyncWorker(constraints: Constraints) =
            PeriodicWorkRequestBuilder<ClearCacheWorker>(
                repeatInterval,
                repeatIntervalTimeUnit
            ).setConstraints(constraints)
                .build()
    }
}