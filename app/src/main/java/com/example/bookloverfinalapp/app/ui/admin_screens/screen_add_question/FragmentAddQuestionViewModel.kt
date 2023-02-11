package com.example.bookloverfinalapp.app.ui.admin_screens.screen_add_question

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.bookloverfinalapp.app.utils.dispatchers.launchSafe
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.AddBookQuestionDomain
import com.example.domain.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentAddQuestionViewModel @Inject constructor(
    private val repository: BooksRepository,
    private val resourceProvider: ResourceProvider,
    private val dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<AddBookQuestion, AddBookQuestionDomain>,
) : BaseViewModel() {

    fun addNewQuestionBook(question: AddBookQuestion) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = { repository.addBookQuestion(question = mapper.map(question)) },
            onError = ::handleError,
            onSuccess = ::handleRequestSuccess
        )
    }

    private fun handleRequestSuccess(unit: Unit) {

    }

    private fun handleError(error: Throwable) {
        emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(error))
    }
}