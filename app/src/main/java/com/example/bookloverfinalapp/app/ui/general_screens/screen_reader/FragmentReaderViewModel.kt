package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader

import com.example.bookloverfinalapp.app.App
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_reader.router.FragmentReaderRouter
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.domain.DispatchersProvider
import com.example.domain.repository.BookThatReadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentReaderViewModel @Inject constructor(
    private val repository: BookThatReadRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val router: FragmentReaderRouter
) : BaseViewModel() {

    fun updateBookReadingProgress(id: String, progress: Int, currentDayProgress: Int) {
        App.applicationScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.updateProgress(id, progress, currentDayProgress) },
            onSuccess = {

            },
            onError = {

            }
        )
    }

    fun navigateToQuestionFragment(book: BookThatRead, chapter: Int, path: String) =
        navigate(router.navigateToQuestionnaireFragment(book, path, chapter))


    fun navigateToBookInfoFragment(bookId: String) =
        navigate(router.navigateToBookInfoFragment(bookId))
}