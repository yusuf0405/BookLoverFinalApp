package com.example.bookloverfinalapp.app.ui.screen_my_books.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadAdapterCommunication
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.DeleteFromMyBooksUseCase
import com.example.domain.domain.interactor.GetBookThatReadUseCase
import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MyBooksViewModel @Inject constructor(
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val deleteFromMyBooksUseCase: DeleteFromMyBooksUseCase,
    private val communication: BooksThatReadAdapterCommunication,
    private val mapper: Mapper<BookThatReadDomain, BookThatReadAdapterModel.Base>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookThatReadAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        bookThatReadUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.map(listOf(BookThatReadAdapterModel.Progress))
                Status.SUCCESS -> {
                    val bookList =
                        resource.data!!.map { studentBookDomain -> mapper.map(studentBookDomain) }
                    if (bookList.isEmpty()) communication.map(listOf(BookThatReadAdapterModel.Empty))
                    else communication.map(bookList)
                }
                Status.ERROR ->
                    communication.map(listOf(BookThatReadAdapterModel.Fail(resource.message!!)))

                Status.NETWORK_ERROR -> {}
            }
        }
    }

    fun deleteBook(id: String) = liveData(context = viewModelScope.coroutineContext) {
        deleteFromMyBooksUseCase.execute(id).flowOn(Dispatchers.IO).collectLatest { resource ->
            if (resource.status == Status.SUCCESS) emit(resource.data!!)
        }
    }

    fun listIsEmpty() = communication.map(listOf(BookThatReadAdapterModel.Empty))

}