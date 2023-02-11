package com.example.data.repository

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.domain.repository.BooksSaveToFileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream

class BooksSaveToFileRepositoryImpl(
    private val context: Context
) : BooksSaveToFileRepository {

    override suspend fun saveBookToFile(
        inputStream: InputStream,
        key: String,
        fileName: String,
        fileSuffix: String
    ) {
        val savedBookPath = savePdfToFile(
            fileName = fileName,
            inputStream = inputStream,
            fileSuffix = fileSuffix
        )
        saveSavedBookPathToSharedPref(key = key, path = savedBookPath)
    }

    override fun fetchSavedBookFilePath(key: String): String? = context
        .getSharedPreferences(key, Context.MODE_PRIVATE)
        .getString(key, null)


    private fun saveSavedBookPathToSharedPref(key: String, path: String) =
        context.getSharedPreferences(
            key,
            Context.MODE_PRIVATE
        ).edit()
            .putString(key, path)
            .apply()


    private suspend fun savePdfToFile(
        inputStream: InputStream,
        fileName: String,
        fileSuffix: String
    ): String {
        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val file = File.createTempFile(fileName, fileSuffix, path)
        val result = withContext(Dispatchers.Default) {
            val save = async {
                return@async inputStream.use { input ->
                    file.outputStream().use { output -> input.copyTo(output) }
                }
            }
            save.await()
            return@withContext file.path
        }
        Log.i("Joseph", result.toString())
        return result
    }
}