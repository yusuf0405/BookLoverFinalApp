package com.example.bookloverfinalapp.app.ui.admin_screens.screen_books

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.BookDomainToBookModelMapper
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.adapter.BookLoadingModel
import com.example.bookloverfinalapp.app.ui.adapter.BookModel
import com.example.bookloverfinalapp.app.ui.adapter.CatEmptyModel
import com.example.bookloverfinalapp.app.ui.adapter.ErrorModel
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_ADMIN_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root.FragmentAdminMainRootDirections
import com.example.bookloverfinalapp.app.utils.communication.BooksCommunication
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.bookloverfinalapp.app.utils.dispatchers.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.*
import com.example.domain.models.BookDomain
import com.example.domain.models.UpdateBookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentBooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getBookForReadingUseCase: GetBookForReadingUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase,
    private val getSearchBookUseCase: GetSearchBookUseCase,
    private val dispatchersProvider: DispatchersProvider,
    private val communication: ItemUiCommunication,
    private val bookCommunication: BooksCommunication,
    private val bookMapper: Mapper<BookDomain, Book>,
    val adapterMapper: Mapper<BookModel, Book>,
) : BaseViewModel() {

    fun booksAdapterModelCollect(
        owner: LifecycleOwner,
        observer: Observer<List<ItemUi>>,
    ) = communication.observe(owner = owner, observer = observer)

    fun booksCollect(owner: LifecycleOwner, observer: Observer<List<Book>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun fetchBooks(schoolId: String) = launchInBackground {
        getAllBooksUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            unravelingResource(resource = resource, schoolId = schoolId)
        }
    }

    private fun unravelingResource(
        resource: Resource<List<BookDomain>>,
        schoolId: String,
    ) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(BookLoadingModel))
            Status.SUCCESS -> {
                communication.put(resource.data!!.map { bookDomain ->
                    BookDomainToBookModelMapper(ADAPTER_ADMIN_BOOK_VIEW_HOLDER).map(
                        bookDomain)
                })
                bookCommunication.put(resource.data!!.map { bookDomain ->
                    bookMapper.map(bookDomain)
                })
            }
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))
            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!
            ) { fetchBooks(schoolId = schoolId) }))
        }
    }

    fun searchBook(searchText: String, schoolId: String) = launchInBackground {
        getSearchBookUseCase.execute(schoolId = schoolId, searchText = searchText)
            .collectLatest { resource ->
                unravelingResource(resource = resource,
                    schoolId = schoolId)
            }
    }


    fun deleteBook(id: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            deleteBookUseCase.execute(id = id).collectLatest { resource ->
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

    fun updateBook(id: String, book: UpdateBookDomain) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            updateBookUseCase.execute(id = id, book = book).collectLatest { resource ->
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

    fun getBookPdf(url: String) =
        liveData(context = viewModelScope.coroutineContext + dispatchersProvider.io()) {
            getBookForReadingUseCase.execute(url = url).collectLatest { resource ->
                when (resource.status) {
                    Status.SUCCESS -> emit(resource.data!!)
                    Status.ERROR -> error(message = resource.message!!)
                }
            }
        }

    fun goChapterFragment(book: Book, path: String) =
        navigate(FragmentAdminMainRootDirections.actionFragmentAdminMainRootToFragmentAdminChapters(
            book = book,
            path = path))

}