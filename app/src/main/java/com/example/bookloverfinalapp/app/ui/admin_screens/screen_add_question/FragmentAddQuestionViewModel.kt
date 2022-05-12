package com.example.bookloverfinalapp.app.ui.admin_screens.screen_add_question

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.AddNewBookQuestionUseCase
import com.example.domain.models.AddBookQuestionDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAddQuestionViewModel @Inject constructor(
    private val addNewBookQuestionUseCase: AddNewBookQuestionUseCase,
    private val mapper: Mapper<AddBookQuestion, AddBookQuestionDomain>,
) : BaseViewModel() {

    fun addNewQuestionBook(question: AddBookQuestion) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            addNewBookQuestionUseCase.execute(question = mapper.map(question))
                .collectLatest { resource ->
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

    fun goBack() = navigateBack()

}