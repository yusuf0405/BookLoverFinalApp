package com.example.bookloverfinalapp.app.ui.screen_book_details.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.utils.communication.BooksAdapterModelCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.AddNewBookThatReadUseCase
import com.example.domain.interactor.GetBookThatReadUseCase
import com.example.domain.interactor.GetMyBookUseCase
import com.example.domain.interactor.GetSimilarBooksUseCase
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentBookInfoViewModel @Inject constructor(
    private val addNewBookThatReadUseCase: AddNewBookThatReadUseCase,
    private val getMyBookUseCase: GetMyBookUseCase,
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val getSimilarBooksUseCase: GetSimilarBooksUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val addBookMapper: Mapper<AddNewBookModel, AddNewBookThatReadDomain>,
    private val communication: BooksAdapterModelCommunication,
    private val mapper: Mapper<BookDomain, BookAdapterModel.Base>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = dispatchers.launchInBackground(viewModelScope) {
        bookThatReadUseCase.execute(id).collectLatest {}

    }

    fun chekIsMyBook(id: String, userId: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            getMyBookUseCase.execute(id = id, userId = userId).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        emit(resource.data!!)
                        showProgressAnimation()
                        dismissProgressDialog()
                    }
                    Status.EMPTY -> {
                        dismissProgressAnimation()
                        dismissProgressDialog()
                    }
                    Status.ERROR -> {
                        error(message = resource.message!!)
                        dismissProgressAnimation()
                        dismissProgressDialog()
                    }
                }
            }
        }

    fun fetchSimilarBook(genres: List<String>, bookId: String) =
        dispatchers.launchInBackground(viewModelScope) {
            getSimilarBooksUseCase.execute(genres = genres, bookId = bookId)
                .collectLatest { resource ->
                    when (resource.status) {
                        Status.LOADING -> communication.put(listOf(BookAdapterModel.Progress))
                        Status.SUCCESS -> communication.put(resource.data!!.map { bookDomain ->
                            mapper.map(bookDomain)
                        })
                        Status.EMPTY -> communication.put(listOf(BookAdapterModel.Empty))
                        Status.ERROR -> communication.put(listOf(BookAdapterModel.Fail(resource.message!!)))
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