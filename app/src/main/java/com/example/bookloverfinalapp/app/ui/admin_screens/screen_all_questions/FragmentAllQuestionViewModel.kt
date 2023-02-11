package com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.models.QuestionModel
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.BookQuestionDomain
import com.example.domain.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentAllQuestionViewModel @Inject constructor(
    private val booksRepository: BooksRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<BookQuestionDomain, QuestionModel>,
    val adapterMapper: Mapper<QuestionModel, BookQuestion>,
) : BaseViewModel() {

    fun fetchBookQuestions(id: String, chapter: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { booksRepository.fetchChapterQuestions(id = id, chapter = chapter) },
            onError = ::handleError,
            onSuccess = ::handleSuccessResult,
        )
    }

    fun deleteQuestion(questionId: String) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { booksRepository.deleteBookQuestion(questionId) },
            onError = ::handleError,
            onSuccess = { data: Unit -> },
        )
    }

    private fun handleSuccessResult(questions: List<BookQuestionDomain>) {

    }

    private fun handleError(exception: Throwable) {

    }

    fun goAddQuestionFragment(id: String, chapter: Int, title: String) =
        navigate(
            FragmentAllQuestionDirections.actionFragmentAllQuestionToFragmentAddQuestion(
                id = id,
                title = title,
                chapter = chapter
            )
        )

    fun goQuestionEditFragment(
        question: BookQuestion,
        title: String,
        chapter: Int,
        id: String,
    ) =
        navigate(
            FragmentAllQuestionDirections.actionFragmentAllQuestionToFragmenAdminEditQuestion(
                question = question, title = title, chapter = chapter, id = id
            )
        )

    fun goBack() = navigateBack()
}