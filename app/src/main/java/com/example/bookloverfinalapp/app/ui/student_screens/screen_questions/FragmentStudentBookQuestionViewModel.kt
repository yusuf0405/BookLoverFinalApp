package com.example.bookloverfinalapp.app.ui.student_screens.screen_questions

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Chapters
import com.example.bookloverfinalapp.app.models.StudentBook
import com.example.bookloverfinalapp.app.utils.extensions.viewModelScope
import com.example.domain.domain.interactor.UpdateChaptersUseCase
import com.example.domain.domain.models.ChaptersDomain
import com.example.domain.models.Resource
import com.example.domain.models.Status
import com.example.domain.models.book.BookQuestion
import com.example.domain.usecase.GetAllChapterQuestionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FragmentStudentBookQuestionViewModel @Inject constructor(
    private val getAllChapterQuestionsUseCase: GetAllChapterQuestionsUseCase,
    private val updateChaptersUseCase: UpdateChaptersUseCase,
) : BaseViewModel() {

    fun getAllChapterQuestions(id: String, chapter: Int): StateFlow<Resource<List<BookQuestion>>> =
        getAllChapterQuestionsUseCase.execute(id = id, chapter = chapter)
            .stateIn(scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.loading())

    fun goChapterFragment(book: StudentBook) {
        navigate(FragmentStudentBookQuestionDirections.
        actionFragmentStudentBookQuestionToFragmentStudentChapterBook(
            book = book))
    }

    fun updateChapters(id: String, chapters: Chapters, book: StudentBook) {
        updateChaptersUseCase.execute(id = id,
            chaptersDomain = ChaptersDomain(chaptersRead = chapters.chapters,
                isReadingPages = chapters.isReadingPages))
            .flowOn(Dispatchers.IO).onEach { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        goChapterFragment(book = book)
                    }
                    else -> {
                        dismissProgressDialog()
                        error(message = resource.message!!)
                        goChapterFragment(book = book)
                    }
                }

            }.viewModelScope(viewModelScope = viewModelScope)
    }

    fun goBack() = navigateBack()

}
