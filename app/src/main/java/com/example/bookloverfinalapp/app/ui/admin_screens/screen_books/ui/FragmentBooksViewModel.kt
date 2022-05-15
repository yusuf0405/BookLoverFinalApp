package com.example.bookloverfinalapp.app.ui.admin_screens.screen_books.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main_root.FragmentAdminMainRootDirections
import com.example.bookloverfinalapp.app.utils.communication.BooksAdapterModelCommunication
import com.example.bookloverfinalapp.app.utils.communication.BooksCommunication
import com.example.domain.Mapper
import com.example.domain.Status
import com.example.domain.interactor.DeleteBookUseCase
import com.example.domain.interactor.GetAllBooksUseCase
import com.example.domain.interactor.GetBookForReadingUseCase
import com.example.domain.interactor.UpdateBookUseCase
import com.example.domain.models.BookDomain
import com.example.domain.models.UpdateBookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentBooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val getBookForReadingUseCase: GetBookForReadingUseCase,
    private val updateBookUseCase: UpdateBookUseCase,
    private val deleteBookUseCase: DeleteBookUseCase,
    private val communication: BooksAdapterModelCommunication,
    private val bookCommunication: BooksCommunication,
    private val mapper: Mapper<BookDomain, BookAdapterModel.Base>,
    private val bookMapper: Mapper<BookDomain, Book>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun bookObserve(owner: LifecycleOwner, observer: Observer<List<Book>>) =
        bookCommunication.observe(owner = owner, observer = observer)

    fun fetchBooks(schoolId: String) = dispatchers.launchInBackground(viewModelScope) {
        getAllBooksUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.put(listOf(BookAdapterModel.Progress))
                Status.SUCCESS -> {
                    communication.put(resource.data!!.map { bookDomain -> mapper.map(bookDomain) })
                    bookCommunication.put(resource.data!!.map { bookDomain ->
                        bookMapper.map(bookDomain)
                    })
                }
                Status.EMPTY -> communication.put(listOf(BookAdapterModel.Empty))
                Status.ERROR -> communication.put(listOf(BookAdapterModel.Fail(resource.message!!)))
            }
        }
    }

    fun deleteBook(id: String) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            deleteBookUseCase.execute(id = id).collectLatest { resource ->
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

    fun updateBook(id: String, book: UpdateBookDomain) =
        liveData(context = viewModelScope.coroutineContext + Dispatchers.IO) {
            updateBookUseCase.execute(id = id, book = book).collectLatest { resource ->
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

    fun getBookPdf(url: String) = liveData(context = viewModelScope.coroutineContext) {
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