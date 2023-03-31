package com.example.bookloverfinalapp.app.ui.service_uplaod.book_save_to_file

import android.content.Context
import androidx.lifecycle.asFlow
import androidx.work.Data
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.bookloverfinalapp.app.ui.service_uplaod.FileDownloadWorker
import com.example.bookloverfinalapp.app.ui.service_uplaod.KEY_FILE_NAME
import com.example.bookloverfinalapp.app.ui.service_uplaod.KEY_FILE_URI
import com.example.bookloverfinalapp.app.ui.service_uplaod.KEY_FILE_URL
import com.example.domain.DispatchersProvider
import com.example.domain.repository.BooksSaveToFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Singleton


@Singleton
class BooksSaveToFileRepositoryImpl(
    private val context: Context,
    private val dispatchersProvider: DispatchersProvider,
) : BooksSaveToFileRepository {

    override suspend fun saveBookToFile(
        fileUrl: String,
        key: String,
        fileName: String,
    ): Flow<String> {
        val workManager = WorkManager.getInstance(context)
        val inputData = Data.Builder()
            .putString(KEY_FILE_URL, fileUrl)
            .putString(KEY_FILE_NAME, fileName)
            .build()
        val worker = FileDownloadWorker.newInstance(inputData)
        workManager.enqueue(worker)
        return workManager.getWorkInfoByIdLiveData(worker.id).asFlow()
            .filter { it != null && it.state == WorkInfo.State.SUCCEEDED }
            .filter { it.outputData.getString(KEY_FILE_URI) != null }
            .map { workInfo ->
                val savedBookPath = workInfo.outputData.getString(KEY_FILE_URI) ?: String()
                saveSavedBookPathToSharedPref(key = key, path = savedBookPath)
                savedBookPath
            }.filter { it.isNotEmpty() }
            .flowOn(dispatchersProvider.default())
    }

    override fun fetchSavedBookFilePath(key: String): String? =
        context
            .getSharedPreferences(key, Context.MODE_PRIVATE)
            .getString(key, null)


    private fun saveSavedBookPathToSharedPref(key: String, path: String) {
        context.getSharedPreferences(key, Context.MODE_PRIVATE)
            .edit()
            .putString(key, path)
            .apply()
    }
}