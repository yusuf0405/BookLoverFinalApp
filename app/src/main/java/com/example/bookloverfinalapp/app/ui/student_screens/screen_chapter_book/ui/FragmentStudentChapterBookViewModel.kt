package com.example.bookloverfinalapp.app.ui.student_screens.screen_chapter_book.ui

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.domain.models.Status
import com.example.domain.domain.interactor.GetBookForReadingUseCase
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.StudentBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentStudentChapterBookViewModel @Inject constructor(
    private val getBookForReadingUseCase: GetBookForReadingUseCase,
) : BaseViewModel() {

    fun getBook(url: String) = liveData(context = viewModelScope.coroutineContext) {
        getBookForReadingUseCase.execute(url = url).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressAnimation()
                Status.SUCCESS -> emit(resource.data!!)
                Status.ERROR -> {
                    error(message = resource.message!!)
                    goBack()
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    goBack()
                }
            }

        }
    }

    fun goReaderFragment(book: StudentBook, startPage: Int, lastPage: Int, chapter: Int) =
        navigate(FragmentStudentChapterBookDirections
            .actionFragmentStudentChapterBookToFragmentStudentReader(book = book,
                startPage = startPage,
                lastPage = lastPage,
                chapter = chapter))

    fun goBack() = navigateBack()
}