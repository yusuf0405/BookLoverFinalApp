package com.example.bookloverfinalapp.app.ui.service_uplaod

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.*
import com.joseph.ui.core.R
import com.example.domain.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val KEY_FILE_URL = "key_file_url"
const val KEY_FILE_TYPE = "key_file_type"
const val KEY_FILE_NAME = "key_file_name"
const val KEY_FILE_URI = "key_file_uri"
const val CHANNEL_NAME = "download_file_worker_demo_channel"
const val CHANNEL_DESCRIPTION = "download_file_worker_demo_description"
const val CHANNEL_ID = "download_file_worker_demo_channel_123456"
const val NOTIFICATION_ID = 1
private const val PDF_SUFFIX = ".pdf"


class FileDownloadWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val fileUrl = inputData.getString(KEY_FILE_URL) ?: ""
        val fileName = inputData.getString(KEY_FILE_NAME) ?: ""
        Log.i("Joseph", "fileUrl = ${fileUrl}")
        Log.i("Joseph", "fileName = ${fileName}")
        if (fileName.isEmpty() || fileUrl.isEmpty()) return Result.failure()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val description = CHANNEL_DESCRIPTION
            val importance = IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            notificationManager?.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(0, 0, true)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())

        val uri = coroutineScope {
            return@coroutineScope async {
                return@async getSavedFileUri(
                    fileName = fileName,
                    fileUrl = fileUrl,
                    context = context
                )
            }
        }.await()

        NotificationManagerCompat.from(context).cancel(NOTIFICATION_ID)
        return if (uri != null) Result.success(Data.Builder().putString(KEY_FILE_URI, uri).build())
        else Result.failure()
    }

    private suspend fun getSavedFileUri(
        fileName: String,
        fileUrl: String,
        context: Context
    ): String? {
        return withContext(Dispatchers.IO) {
            val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File.createTempFile(fileName, PDF_SUFFIX, path)
            when (val response = fetchBookPdfFileForReading(fileUrl)) {
                is RequestState.Error -> Unit
                is RequestState.Success -> startSavingFileToExternalStorage(response.data, file)
            }
            return@withContext file.path
        }
    }

    private suspend fun startSavingFileToExternalStorage(
        inputStream: InputStream,
        file: File
    ) = withContext(Dispatchers.Default) {
        val save = async {
            return@async inputStream.use { input ->
                file.outputStream().use { output -> input.copyTo(output) }
            }
        }
        save.await()
    }


    private suspend fun fetchBookPdfFileForReading(url: String): RequestState<InputStream> =
        coroutineScope {
            return@coroutineScope withContext(Dispatchers.IO) {
                return@withContext try {
                    val urlConnection = URL(url).openConnection() as HttpsURLConnection
                    if (urlConnection.responseCode == 200) {
                        RequestState.Success(BufferedInputStream(urlConnection.inputStream))
                    } else RequestState.Error(IllegalStateException())
                } catch (error: Throwable) {
                    RequestState.Error(error)
                }
            }
        }

    companion object {
        private val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        @JvmStatic
        fun newInstance(inputData: Data) = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()
    }
}

enum class UploadFileType {
    PDF
}