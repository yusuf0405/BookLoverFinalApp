package com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.utils.communication.BooksQuestionCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.DeleteBookQuestionUseCase
import com.example.domain.interactor.GetAllChapterQuestionsUseCase
import com.example.domain.models.BookQuestionDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAllQuestionViewModel @Inject constructor(
    private val getAllChapterQuestionsUseCase: GetAllChapterQuestionsUseCase,
    private val deleteBookQuestionUseCase: DeleteBookQuestionUseCase,
    private val communication: BooksQuestionCommunication,
    private val mapper: Mapper<BookQuestionDomain, BookQuestion>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookQuestion>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchBookQuestions(id: String, chapter: Int) =
        dispatchers.launchInBackground(viewModelScope) {
            getAllChapterQuestionsUseCase.execute(id = id, chapter = chapter)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.LOADING -> showProgressDialog()

                        Status.SUCCESS -> {
                            communication.put(resource.data!!.map { bookQuestionDomain -> mapper.map(bookQuestionDomain) })
                            dismissProgressDialog()
                        }
                        Status.ERROR -> {
                            error(message = resource.message!!)
                            dismissProgressDialog()

                        }
                        Status.EMPTY -> {
                            error(message = "No Questions")
                            dismissProgressDialog()
                        }
                    }
                }
        }

    fun deleteQuestion(id: String) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            deleteBookQuestionUseCase.execute(id = id).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        emit(Unit)
                        dismissProgressDialog()
                    }
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressDialog()
                    }
                }
            }
        }

    fun goAddQuestionFragment(id: String, chapter: Int, title: String) =
        navigate(FragmentAllQuestionDirections.actionFragmentAllQuestionToFragmentAddQuestion(
            id = id,
            title = title,
            chapter = chapter))

    fun goQuestionEditFragment(question: BookQuestion, title: String, chapter: Int, id: String) =
        navigate(FragmentAllQuestionDirections.actionFragmentAllQuestionToFragmenAdminEditQuestion(
            question = question, title = title, chapter = chapter, id = id))

    fun goBack() = navigateBack()
}