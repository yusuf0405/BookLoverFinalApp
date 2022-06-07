package com.example.bookloverfinalapp.app.ui.general_screens.screen_reader

import com.example.bookloverfinalapp.app.App
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.domain.Status
import com.example.domain.interactor.UpdateProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    ) = App.applicationScope.launch {
        updateProgressStudentBookUseCase.execute(id = id,
            progress = progress).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> dismissProgressDialog()
                Status.ERROR -> {
                    dismissProgressDialog()
                    error(message = resource.message!!)
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