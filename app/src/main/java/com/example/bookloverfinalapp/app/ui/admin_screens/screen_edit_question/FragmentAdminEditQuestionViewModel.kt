package com.example.bookloverfinalapp.app.ui.admin_screens.screen_edit_question

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.UpdateBookQuestionUseCase
import com.example.domain.models.AddBookQuestionDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAdminEditQuestionViewModel @Inject constructor(
    private val updateBookQuestionUseCase: UpdateBookQuestionUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val mapper: Mapper<AddBookQuestion, AddBookQuestionDomain>,
) : BaseViewModel() {


    fun updateQuestion(id: String, question: AddBookQuestion) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            updateBookQuestionUseCase.execute(question = mapper.map(question), id = id)
                .collectLatest { resource ->
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


    fun goBack() = navigateBack()
}