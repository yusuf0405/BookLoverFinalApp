package com.example.bookloverfinalapp.app.ui.screen_reader

import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.domain.Status
import com.example.domain.interactor.UpdateProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentReaderViewModel @Inject constructor(
    private val updateProgressStudentBookUseCase: UpdateProgressUseCase,
) : BaseViewModel() {


    fun updateProgress(
        id: String,
        progress: Int,
    ) = GlobalScope.launch {
        updateProgressStudentBookUseCase.execute(id = id,
            progress = progress).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    error(message = "Изменение успешно сохронено")
                    dismissProgressDialog()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }
    }


    fun goQuestionFragment(book: BookThatRead, chapter: Int, path: String) =
        navigate(FragmentReaderDirections.actionStudentFragmentReaderToFragmentBookQuestion(
            book = book,
            chapter = chapter,
            path = path
        ))

    fun goBack() = navigateBack()
}