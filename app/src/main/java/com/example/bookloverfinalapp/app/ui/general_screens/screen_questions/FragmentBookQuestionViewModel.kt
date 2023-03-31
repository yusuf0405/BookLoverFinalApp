package com.example.bookloverfinalapp.app.ui.general_screens.screen_questions

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_questions.router.FragmentBookQuestionRouter
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.BookQuestionDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class FragmentBookQuestionViewModel @Inject constructor(
    private val bookThatReadRepository: BookThatReadRepository,
    private val booksRepository: BooksRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val questionDomainToUiMapper: Mapper<BookQuestionDomain, BookQuestion>,
    private val router: FragmentBookQuestionRouter
) : BaseViewModel() {

    private val chapterParametersFlow = MutableStateFlow(Pair(String(), String()))

    private val _isStartFetchQuestionsFlow = createMutableSharedFlowAsSingleLiveEvent<Boolean>()
    val isStartFetchQuestionsFlow: SharedFlow<Boolean> get() = _isStartFetchQuestionsFlow.asSharedFlow()

    private val _chapterAllQuestionsFlow = createMutableSharedFlowAsLiveData<List<BookQuestion>>()
    val chapterAllQuestionsFlow get() = _chapterAllQuestionsFlow.asSharedFlow()

    private val _isErrorFlow = createMutableSharedFlowAsSingleLiveEvent<String>()
    val isErrorFlow: SharedFlow<String> get() = _isErrorFlow.asSharedFlow()

    fun getAllChapterQuestions(
        id: String,
        chapter: String,
    ) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { booksRepository.fetchChapterQuestions(id, chapter) },
            onStart = { _isStartFetchQuestionsFlow.tryEmit(true) },
            onSuccess = { _chapterAllQuestionsFlow.tryEmit(it.map(questionDomainToUiMapper::map)) },
            onError = {
                _isErrorFlow.tryEmit(resourceProvider.fetchErrorMessage(it))
                emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it))
            },
        )
    }

    fun updateChapters(
        id: String,
        chapters: Int,
        isReadingPages: List<Boolean>,
        book: BookThatRead,
        path: String,
    ) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = {
                bookThatReadRepository.updateChapters(
                    id = id,
                    isReadingPages = isReadingPages,
                    chapters = chapters
                )
            },
            onSuccess = {
                navigateToChapterFragment(book = book, path = path)
            },
            onError = {
                _isErrorFlow.tryEmit(resourceProvider.fetchErrorMessage(it))
                emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it))
            }
        )
    }

    fun navigateToChapterFragment(book: BookThatRead, path: String) =
        navigate(router.navigateToBookReadFragment(savedBook = book, path = path))

}
