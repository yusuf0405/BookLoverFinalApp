package com.example.bookloverfinalapp.app.ui.screen_book_details.ui

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.domain.Mapper
import com.example.domain.interactor.AddNewBookThatReadUseCase
import com.example.domain.interactor.GetMyBookUseCase
import com.example.domain.models.AddNewBookDomain
import com.example.domain.models.BookThatReadDomain
import com.example.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentBookDetailsViewModel @Inject constructor(
    private val addNewBookThatReadUseCase: AddNewBookThatReadUseCase,
    private val getMyBookUseCase: GetMyBookUseCase,
    private val mapper: Mapper<BookThatReadDomain, BookThatRead>,
    private val addBookMapper: Mapper<AddNewBookModel, AddNewBookDomain>,
) : BaseViewModel() {


    fun chekIsMyBook(id: String) = liveData {
        getMyBookUseCase.execute(id = id).collectLatest { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    emit(mapper.map(resource.data!!))
                    showProgressAnimation()
                }
                Status.ERROR -> {
                    dismissProgressAnimation()
                }
                else -> {}
            }
        }
    }

    fun addNewBook(book: AddNewBookModel) = liveData(context = viewModelScope.coroutineContext) {
        addNewBookThatReadUseCase.execute(book = addBookMapper.map(book))
            .collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        emit(Unit)
                        dismissProgressDialog()
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

