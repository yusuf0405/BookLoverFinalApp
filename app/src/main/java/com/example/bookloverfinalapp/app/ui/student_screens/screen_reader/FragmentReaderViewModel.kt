package com.example.bookloverfinalapp.app.ui.student_screens.screen_reader

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Progress
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.domain.interactor.UpdateProgressUseCase
import com.example.domain.domain.models.ProgressDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class FragmentReaderViewModel @Inject constructor(
    private val updateProgressStudentBookUseCase: UpdateProgressUseCase,
) : BaseViewModel() {


    fun updateProgress(
        id: String,
        progress: Progress,
        type: Boolean,
        book: BookThatRead,
        chapter: Int,
    ) {
        updateProgressStudentBookUseCase.execute(id = id,
            progress = ProgressDomain(progress = progress.progress)).onEach { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
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


    private fun goQuestionFragment(book: BookThatRead, chapter: Int) =
        navigate(FragmentReaderDirections.actionFragmentReaderToFragmentBookQuestion(
            book = book,
            chapter = chapter,
        ))

    fun goBack() = navigateBack()
}