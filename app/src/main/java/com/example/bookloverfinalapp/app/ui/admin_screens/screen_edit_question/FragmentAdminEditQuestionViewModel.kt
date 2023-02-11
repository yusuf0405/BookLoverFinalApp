package com.example.bookloverfinalapp.app.ui.admin_screens.screen_edit_question

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
class FragmentAdminEditQuestionViewModel @Inject constructor(
    private val repository: BooksRepository,
    private val resourceProvider: ResourceProvider,
    private val dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<AddBookQuestion, AddBookQuestionDomain>,
) : BaseViewModel() {


    fun updateQuestion(id: String, question: AddBookQuestion) {
        viewModelScope.launchSafe(
            dispatcher = dispatchersProvider.io(),
            safeAction = {
                repository.updateBookQuestion(
                    question = mapper.map(question),
                    id = id
                )
            },
            onSuccess = {

            },
            onError = { emitToErrorMessageFlow(resourceProvider.fetchIdErrorMessage(it)) }

        )
    }
}