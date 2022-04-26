package com.example.bookloverfinalapp.app.ui.student_screens.screen_reader

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.data.data.cache.db.StudentBooksDao
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.book.BookUpdateProgress
import com.example.domain.domain.interactor.GetBookForReadingUseCase
import com.example.domain.usecase.UpdateProgressStudentBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class FragmentStudentReaderViewModel @Inject constructor(
    private val getBookForReadingUseCase: GetBookForReadingUseCase,
    private val updateProgressStudentBookUseCase: UpdateProgressStudentBookUseCase,
    private val dao: StudentBooksDao,
) : BaseViewModel() {

    fun getBook(url: String): StateFlow<Resource<InputStream>> =
        getBookForReadingUseCase.execute(url = url)
            .stateIn(scope = viewModelScope, started = SharingStarted.Lazily,
                initialValue = Resource.loading())

    fun updateProgress(
        id: String,
        progress: BookUpdateProgress,
        type: Boolean,
        book: StudentBook,
        chapter: Int,
    ) {
        updateProgressStudentBookUseCase.execute(id = id, progress = progress).onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dispatchers.launchInBackground(viewModelScope) {
                        dao.updateProgress(progress = progress.progress, id = id)
                    }
                    if (type) goQuestionFragment(book = book, chapter = chapter)
                    else error(message = "Изменение успешно сохронено")
                    dismissProgressDialog()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    dismissProgressDialog()
                }
            }
        }.viewModelScope(viewModelScope = viewModelScope)
    }


    private fun goQuestionFragment(book: StudentBook, chapter: Int) =
        navigate(FragmentStudentReaderDirections.actionFragmentStudentReaderToFragmentStudentBookQuestion(
            book = book,
            chapter = chapter,
        ))

    fun goBack() = navigateBack()
}