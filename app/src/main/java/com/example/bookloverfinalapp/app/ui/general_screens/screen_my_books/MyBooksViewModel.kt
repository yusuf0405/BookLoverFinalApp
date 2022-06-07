package com.example.bookloverfinalapp.app.ui.general_screens.screen_my_books

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.*
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.ui.adapter.BookThatReadLoadingModel
import com.example.bookloverfinalapp.app.ui.adapter.BookThatReadModel
import com.example.bookloverfinalapp.app.ui.adapter.CatEmptyModel
import com.example.bookloverfinalapp.app.ui.adapter.ErrorModel
import com.example.bookloverfinalapp.app.utils.communication.BooksThatReadCommunication
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.BooksThatReadOnRefreshUseCase
import com.example.domain.interactor.DeleteFromMyBooksUseCase
import com.example.domain.interactor.GetBookForReadingUseCase
import com.example.domain.interactor.GetBookThatReadUseCase
import com.example.domain.models.BookThatReadDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MyBooksViewModel @Inject constructor(
    private val bookThatReadUseCase: GetBookThatReadUseCase,
    private val deleteFromMyBooksUseCase: DeleteFromMyBooksUseCase,
    private val getBookForReadingUseCase: GetBookForReadingUseCase,
    private val onRefreshUseCase: BooksThatReadOnRefreshUseCase,
    private val communication: ItemUiCommunication,
    private val bookCommunication: BooksThatReadCommunication,
    private val mapper: Mapper<BookThatReadDomain, BookThatReadModel>,
    private val bookMapper: Mapper<BookThatReadDomain, BookThatRead>,
    val adapterMapper: Mapper<BookThatReadModel, BookThatRead>,
) : BaseViewModel() {

    fun bookThatReadAdapterModelCollect(
        owner: LifecycleOwner,
        observer: Observer<List<ItemUi>>,
    ) = communication.observe(owner = owner, observer = observer)

    fun bookObserve(owner: LifecycleOwner, observer: Observer<List<BookThatRead>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = launchInBackground {
        bookThatReadUseCase.execute(id).collectLatest { resource ->
            fetchResource(resource = resource, id = id)
        }
    }

    fun onRefresh(id: String) = launchInBackground {
        onRefreshUseCase.execute(id).collectLatest { resource ->
            fetchResource(resource = resource, id = id)
        }
    }

    private fun fetchResource(resource: Resource<List<BookThatReadDomain>>, id: String) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(BookThatReadLoadingModel))

            Status.SUCCESS -> {
                communication.put(resource.data!!.map { bookDomain ->
                    mapper.map(bookDomain)
                })
                bookCommunication.put(resource.data!!.map { bookDomain ->
                    bookMapper.map(bookDomain)
                })
            }
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))

            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!) {
                onRefresh(id = id)
            }))
        }
    }

    fun deleteBook(id: String) = liveData(context = viewModelScope.coroutineContext) {
        deleteFromMyBooksUseCase.execute(id).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> showProgressDialog()
                Status.SUCCESS -> {
                    dismissProgressDialog()
                    emit(resource.data)
                }
                Status.ERROR -> {
                    dismissProgressDialog()
                    error(message = resource.message!!)
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

    fun listIsEmpty() =
        launchInBackground { communication.put(listOf(CatEmptyModel)) }

}