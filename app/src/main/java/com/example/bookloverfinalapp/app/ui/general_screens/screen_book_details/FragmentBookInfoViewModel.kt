package com.example.bookloverfinalapp.app.ui.general_screens.screen_book_details

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.BookDomainToBookModelMapper
import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookModel
import com.example.bookloverfinalapp.app.models.CubeEmptyModel
import com.example.bookloverfinalapp.app.models.ErrorModel
import com.example.bookloverfinalapp.app.models.SimilarBookLoadingModel
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_SIMILAR_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Resource
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
    private val communication: ItemUiCommunication,
    val adapterMapper: Mapper<BookModel, Book>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchMyBook(id: String) = launchInBackground {
        bookThatReadUseCase.execute(id).collectLatest {}
    }

    fun chekIsMyBook(id: String, userId: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            getMyBookUseCase.execute(id = id, userId = userId).collectLatest { resource ->
                when (resource.status) {
                    Status.LOADING -> showProgressDialog()
                    Status.SUCCESS -> {
                        dismissProgressDialog()
                        emit(resource.data!!)
                        showProgressAnimation()
                    }
                    Status.EMPTY -> {
                        dismissProgressDialog()
                        dismissProgressAnimation()
                    }
                    Status.ERROR -> {
                        dismissProgressDialog()
                        error(message = resource.message!!)
                        dismissProgressAnimation()
                    }
                }
            }
        }

    fun fetchSimilarBook(genres: List<String>, bookId: String) = launchInBackground {
        getSimilarBooksUseCase.execute(genres = genres, bookId = bookId)
            .collectLatest { resource ->
                unravelingResource(resource = resource,
                    genres = genres,
                    bookId = bookId)
            }
    }

    private fun unravelingResource(
        resource: Resource<List<BookDomain>>,
        genres: List<String>,
        bookId: String,
    ) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(SimilarBookLoadingModel))
            Status.SUCCESS -> communication.put(resource.data!!.map { bookDomain ->
                BookDomainToBookModelMapper(ADAPTER_SIMILAR_BOOK_VIEW_HOLDER).map(bookDomain)
            })
            Status.EMPTY -> communication.put(listOf(CubeEmptyModel))
            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!) {
                fetchSimilarBook(bookId = bookId,
                    genres = genres)
            }))
        }
    }

    fun addNewBook(book: AddNewBookModel) = liveData(context = viewModelScope.coroutineContext) {
        addNewBookThatReadUseCase.execute(book = addBookMapper.map(book))
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

    fun goGenreSimilarBooks(genre: String) =
        navigate(FragmentBookInfoDirections.actionFragmentBookInfoToFragmentGenreSimilarBooks(genre = genre))

    fun goBack() = navigateBack()

}