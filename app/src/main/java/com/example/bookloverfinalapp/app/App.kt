package com.example.bookloverfinalapp.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.WorkManager
import com.example.bookloverfinalapp.app.di.APPLICATION_ID
import com.example.bookloverfinalapp.app.utils.cons.CLIENT_KEY
import com.example.bookloverfinalapp.app.workers.ClearCacheWorker
import com.parse.Parse
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        applicationScope =
            CoroutineScope(context = SupervisorJob() + kotlinx.coroutines.Dispatchers.Main)
        Lingver.init(this)
        Parse.enableLocalDatastore(this)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(APPLICATION_ID)
                .clientKey(CLIENT_KEY)
                .server(PARSE_URL)
                .build()
        )
        val workManager = WorkManager.getInstance(this)

        startSyncWorker(workManager)
    }

    private fun startSyncWorker(workManager: WorkManager) {
        workManager.enqueueUniquePeriodicWork(
            ClearCacheWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            ClearCacheWorker.getSyncWorker(createConstraintsWithInternetConnectedAndBatteryCharged())
        )
    }

    private fun createConstraintsWithInternetConnectedAndBatteryCharged() =
        Constraints
            .Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    companion object {
        private const val PARSE_URL = "https://parseapi.back4app.com"
        lateinit var applicationScope: CoroutineScope
    }

    override val workManagerConfiguration: Configuration = Configuration.Builder()
        .setWorkerFactory(HiltWorkerFactory.getDefaultWorkerFactory())
        .build()
}