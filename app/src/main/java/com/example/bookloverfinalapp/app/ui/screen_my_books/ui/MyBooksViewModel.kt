package com.example.bookloverfinalapp.app.ui.screen_my_books.ui

import androidx.lifecycle.*
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadAdapterCommunication
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.domain.Mapper
import com.example.domain.interactor.DeleteFromMyBooksUseCase
import com.example.domain.interactor.GetBookForReadingUseCase
import com.example.domain.interactor.GetBookThatReadUseCase
import com.example.domain.models.BookThatReadDomain
import com.example.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MyBooksViewModel @Inject constructor(
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val deleteFromMyBooksUseCase: DeleteFromMyBooksUseCase,
    private val getBookForReadingUseCase: GetBookForReadingUseCase,
    private val communication: BooksThatReadAdapterCommunication,
    private val bookCommunication: BooksThatReadCommunication,
    private val mapper: Mapper<BookThatReadDomain, BookThatReadAdapterModel.Base>,
    private val bookMapper: Mapper<BookThatReadDomain, BookThatRead>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookThatReadAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun bookObserve(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        bookThatReadUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.put(listOf(BookThatReadAdapterModel.Progress))
                Status.SUCCESS -> {
                    communication.put(resource.data!!.map { bookDomain -> mapper.map(bookDomain) })
                    bookCommunication.put(resource.data!!.map { bookThatReadDomain -> bookMapper.map(bookThatReadDomain) })
                }
                Status.EMPTY -> communication.put(listOf(BookThatReadAdapterModel.Empty))
                Status.ERROR -> communication.put(listOf(BookThatReadAdapterModel.Fail(resource.message!!)))
            }
        }
    }

    fun deleteBook(id: String) = liveData(context = viewModelScope.coroutineContext) {
        deleteFromMyBooksUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    emit(resource.data)
                    dismissProgressDialog()
                }
                Status.ERROR -> {
                    error(message = resource.message!!)
                    dismissProgressDialog()
                }
            }
        }
    }

    fun getBookPdf(url: String) = liveData(context = viewModelScope.coroutineContext) {
        getBookForReadingUseCase.execute(url = url).collectLatest { resource ->
            when (resource.status) {
                Status.SUCCESS -> emit(resource.data!!)
                Status.ERROR -> error(message = resource.message!!)
            }
        }
    }

    fun listIsEmpty() = communication.put(listOf(BookThatReadAdapterModel.Empty))

}