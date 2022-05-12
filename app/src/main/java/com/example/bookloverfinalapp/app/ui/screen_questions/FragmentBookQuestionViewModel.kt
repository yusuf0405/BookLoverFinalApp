package com.example.bookloverfinalapp.app.ui.screen_questions

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.GetAllChapterQuestionsUseCase
import com.example.domain.interactor.UpdateChaptersUseCase
import com.example.domain.models.BookQuestionDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FragmentBookQuestionViewModel @Inject constructor(
    private val getAllChapterQuestionsUseCase: GetAllChapterQuestionsUseCase,
    private val updateChaptersUseCase: UpdateChaptersUseCase,
    val mapper: Mapper<BookQuestionDomain, BookQuestion>,
) : BaseViewModel() {

    fun getAllChapterQuestions(
        id: String,
        chapter: Int,
    ): StateFlow<Resource<List<BookQuestionDomain>>> =
        getAllChapterQuestionsUseCase.execute(id = id, chapter = chapter)
            .stateIn(scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.loading())

    fun goChapterFragment(book: BookThatRead, path: String) {
        navigate(FragmentBookQuestionDirections.actionFragmentBookQuestionToFragmentChapterBook(
            book = book, path = path))
    }

    fun updateChapters(
        id: String, chapters: Int, isReadingPages: List<Boolean>, book: BookThatRead, path: String,
    ) {
        updateChaptersUseCase.execute(id = id, isReadingPages = isReadingPages, chapters = chapters)
            .onEach { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        goChapterFragment(book = book, path = path)
                    }
                    else -> {
                        dismissProgressDialog()
                        error(message = resource.message!!)
                        goChapterFragment(book = book, path = path)
                    }
                }

            }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun goBack() = navigateBack()

}
