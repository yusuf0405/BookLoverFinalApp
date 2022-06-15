package com.example.bookloverfinalapp.app.ui.admin_screens.screen_all_questions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.*
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.bookloverfinalapp.app.ui.adapter.*
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.DeleteBookQuestionUseCase
import com.example.domain.interactor.GetAllChapterQuestionsUseCase
import com.example.domain.models.BookQuestionDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAllQuestionViewModel @Inject constructor(
    private val getAllChapterQuestionsUseCase: GetAllChapterQuestionsUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val deleteBookQuestionUseCase: DeleteBookQuestionUseCase,
    private val communication: ItemUiCommunication,
    private val mapper: Mapper<BookQuestionDomain, QuestionModel>,
    val adapterMapper: Mapper<QuestionModel, BookQuestion>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchBookQuestions(id: String, chapter: Int) = launchInBackground {
        getAllChapterQuestionsUseCase.execute(id = id, chapter = chapter)
            .collectLatest { resource ->
                unravelingResource(resource = resource, id = id, chapter = chapter)
            }
    }

    private fun unravelingResource(
        resource: Resource<List<BookQuestionDomain>>,
        id: String,
        chapter: Int) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(QuestionLoadingModel))

            Status.SUCCESS -> communication.put(resource.data!!.map { bookQuestionDomain ->
                mapper.map(bookQuestionDomain)
            })

            Status.ERROR -> communication.put(listOf(ErrorModel(
                resource.message!!) { fetchBookQuestions(id = id, chapter = chapter) }))

            Status.EMPTY -> communication.put(listOf(CubeEmptyModel))
        }
    }

    fun deleteQuestion(id: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            deleteBookQuestionUseCase.execute(id = id).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        emit(Unit)
                    }
                    Status.ERROR -> {
                        dismissProgressDialog()
                        error(message = resource.message!!)
                    }
                }
            }
        }

    fun goAddQuestionFragment(id: String, chapter: Int, title: String) =
        navigate(FragmentAllQuestionDirections.actionFragmentAllQuestionToFragmentAddQuestion(
            id = id,
            title = title,
            chapter = chapter))

    fun goQuestionEditFragment(
        question: BookQuestion,
        title: String,
        chapter: Int,
        id: String,
    ) =
        navigate(FragmentAllQuestionDirections.actionFragmentAllQuestionToFragmenAdminEditQuestion(
            question = question, title = title, chapter = chapter, id = id))

    fun goBack() = navigateBack()
}