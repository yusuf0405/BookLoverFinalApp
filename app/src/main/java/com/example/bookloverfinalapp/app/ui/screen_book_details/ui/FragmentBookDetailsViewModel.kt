package com.example.bookloverfinalapp.app.ui.screen_book_details.ui

import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.AddNewBookThatReadUseCase
import com.example.domain.domain.interactor.GetBookForReadingUseCase
import com.example.domain.domain.interactor.GetMyBookUseCase
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.models.Status
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
        addNewBookThatReadUseCase.execute(book = addBookMapper.map(book)).collectLatest { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    emit(true)
                    dismissProgressDialog()
                }

                Status.ERROR -> {
                    dismissProgressDialog()
                    error(message = resource.message!!)
                }
                Status.NETWORK_ERROR -> {
                    networkError()
                    goBack()
                }
                else -> {}
            }
        }
    }

    fun goBack() = navigateBack()

}

