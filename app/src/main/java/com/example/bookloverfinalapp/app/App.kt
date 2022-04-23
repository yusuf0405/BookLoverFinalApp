package com.example.bookloverfinalapp.app

import android.app.Application
import com.example.data.data.cache.db.BooksDao
import com.example.data.data.cache.db.StudentBooksDao
import com.example.bookloverfinalapp.app.utils.APPLICATION_ID
import com.example.bookloverfinalapp.app.utils.CLIENT_KEY
import com.example.bookloverfinalapp.app.utils.Dispatchers
import com.parse.Parse
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var studentBooksDao: StudentBooksDao

    @Inject
    lateinit var booksDao: BooksDao

    @Inject
    lateinit var dispatchers: Dispatchers

    private val applicationScope =
        CoroutineScope(SupervisorJob() + kotlinx.coroutines.Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()
        Parse.enableLocalDatastore(this)
        Parse.initialize(Parse.Configuration.Builder(this)
            .applicationId(APPLICATION_ID)
            .clientKey(CLIENT_KEY)
            .server("https://parseapi.back4app.com")
            .build()
        )
        dispatchers.launchInBackground(scope = applicationScope) {
            booksDao.clearTable()
            studentBooksDao.clearTable()
        }
    }
}