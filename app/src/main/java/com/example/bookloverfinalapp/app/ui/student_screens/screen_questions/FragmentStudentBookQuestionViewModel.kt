package com.example.bookloverfinalapp.app.ui.student_screens.screen_questions

import androidx.lifecycle.viewModelScope
import com.example.domain.models.Resource
import com.example.domain.models.book.BookQuestion
import com.example.domain.usecase.GetAllChapterQuestionsUseCase
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.StudentBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class FragmentStudentBookQuestionViewModel @Inject constructor(
    private val getAllChapterQuestionsUseCase: GetAllChapterQuestionsUseCase,
) : BaseViewModel() {

    fun getAllChapterQuestions(id: String, chapter: Int): StateFlow<Resource<List<BookQuestion>>> =
        getAllChapterQuestionsUseCase.execute(id = id, chapter = chapter)
            .stateIn(scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Resource.loading())

    fun goChapterFragment(book: StudentBook) {
        navigate(FragmentStudentBookQuestionDirections.actionFragmentStudentBookQuestionToFragmentStudentChapterBook(
            book = book))
    }

    fun goBack() = navigateBack()

}
