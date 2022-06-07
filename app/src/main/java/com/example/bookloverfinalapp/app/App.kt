package com.example.bookloverfinalapp.app

import android.app.Application
import com.example.bookloverfinalapp.app.utils.cons.APPLICATION_ID
import com.example.bookloverfinalapp.app.utils.cons.CLIENT_KEY
import com.example.bookloverfinalapp.app.utils.dispatchers.Dispatchers
import com.example.bookloverfinalapp.app.utils.navigation.NavigationManager
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.domain.interactor.ClearBooksCacheUseCase
import com.example.domain.interactor.ClearBooksThatReadCacheUseCase
import com.example.domain.interactor.ClearClassCacheUseCase
import com.example.domain.interactor.ClearStudentsCacheUseCase
import com.parse.Parse
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var clearStudentsCacheUseCase: ClearStudentsCacheUseCase

    @Inject
    lateinit var clearClassCacheUseCase: ClearClassCacheUseCase

    @Inject
    lateinit var clearBooksThatReadCacheUseCase: ClearBooksThatReadCacheUseCase

    @Inject
    lateinit var clearBooksCacheUseCase: ClearBooksCacheUseCase

    @Inject
    lateinit var dispatchers: Dispatchers


    override fun onCreate() {
        super.onCreate()

        applicationScope =
            CoroutineScope(context = SupervisorJob() + kotlinx.coroutines.Dispatchers.Main)

        Lingver.init(this)

        Parse.enableLocalDatastore(this)

        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(APPLICATION_ID)
            .clientKey(CLIENT_KEY)
            .server("https://parseapi.back4app.com")
            .build()
        )
        SharedPreferences().saveIsFilter(false, this)

        if (NavigationManager().isOnline(context = this)) {
            dispatchers.launchInBackground(scope = applicationScope) {
                clearBooksCacheUseCase.execute()
                clearBooksThatReadCacheUseCase.execute()
                clearClassCacheUseCase.execute()
                clearStudentsCacheUseCase.execute()
            }
        }
    }

    companion object {
        lateinit var applicationScope: CoroutineScope
    }
}