package com.example.bookloverfinalapp.app.ui.screen_questions

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.Chapters
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.GetAllChapterQuestionsUseCase
import com.example.domain.domain.interactor.UpdateChaptersUseCase
import com.example.domain.domain.models.BookQuestionDomain
import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.models.Resource
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    fun updateChapters(id: String, chapters: Chapters, book: BookThatRead, path: String) {
        updateChaptersUseCase.execute(id = id,
            chaptersDomain = ChaptersDomain(chaptersRead = chapters.chapters,
                isReadingPages = chapters.isReadingPages))
            .flowOn(Dispatchers.IO).onEach { resource ->
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
