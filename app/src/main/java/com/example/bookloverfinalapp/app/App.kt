package com.example.bookloverfinalapp.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.example.bookloverfinalapp.app.di.APPLICATION_ID
import com.example.bookloverfinalapp.app.workers.ClearCacheWorker
import com.example.bookloverfinalapp.app.utils.cons.CLIENT_KEY
import com.parse.Parse
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject


@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

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
                .server("https://parseapi.back4app.com")
                .build()
        )
        startSyncWorker()
    }

    private fun startSyncWorker() {
        val workManager = WorkManager.getInstance(this)
        workManager.enqueueUniquePeriodicWork(
            ClearCacheWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            ClearCacheWorker.getSyncWorker(createConstraintsWithInternetConnectedAndBatteryCharged())
        )
    }

    private fun createConstraintsWithInternetConnectedAndBatteryCharged() =
        Constraints
            .Builder()
            .build()

    companion object {
        lateinit var applicationScope: CoroutineScope
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}