package com.example.bookloverfinalapp.app.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.UserCacheRepository
import com.example.bookloverfinalapp.app.ui.service_uplaod.book_save_to_file.CheckBookIsSavedAndSaveUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.CancellationException

@HiltWorker
class CheckBookIsSaveWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val checkBookIsSavedAndSaveUseCase: CheckBookIsSavedAndSaveUseCase,
    private val userCacheRepository: UserCacheRepository,
    private val savedBooksRepository: BookThatReadRepository,
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val id = userCacheRepository.fetchCurrentUserFromCache().id
            val savedBooks = savedBooksRepository
                .fetchUserAllBooksThatReadByUserId(id)
                .firstOrNull() ?: return Result.failure()
            checkBookIsSavedAndSaveUseCase.invoke(savedBooks)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return if (runAttemptCount < 3) Result.retry()
            else Result.failure()
        }
        return Result.success()
    }

    companion object {
        private val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        @JvmStatic
        fun getCheckBookIsSaveWorker() =
            OneTimeWorkRequestBuilder<CheckBookIsSaveWorker>()
                .setConstraints(constraints)
                .build()
    }
}