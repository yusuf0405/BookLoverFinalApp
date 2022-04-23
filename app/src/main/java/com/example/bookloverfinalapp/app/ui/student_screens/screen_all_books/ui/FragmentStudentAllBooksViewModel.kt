package com.example.bookloverfinalapp.app.ui.student_screens.screen_all_books.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.ui.student_screens.screen_book_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.communication.BooksCommunication
import com.example.domain.domain.Mapper
import com.example.domain.domain.interactor.GetAllBooksUseCase
import com.example.domain.domain.models.BookDomain
import com.example.domain.models.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class FragmentStudentAllBooksViewModel @Inject constructor(
    private val useCaseGetAll: GetAllBooksUseCase,
    private val communication: BooksCommunication,
    private val mapper: Mapper<BookDomain, BookAdapterModel.Base>,
) : BaseViewModel() {


    fun observe(owner: LifecycleOwner, observer: Observer<List<BookAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun goBookDetailsFragment(book: Book) =
        navigate(FragmentRootStudentBookDirections.actionFragmentRootStudentBookToFragmentStudentBookDetails2(
            book = book))


    fun fetchBooks() = dispatchers.launchInBackground(viewModelScope) {
        useCaseGetAll.execute().collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.map(listOf(BookAdapterModel.Progress))
                Status.SUCCESS ->
                    communication.map(resource.data!!.map { bookDomain -> mapper.map(bookDomain) })
                Status.ERROR -> communication.map(listOf(BookAdapterModel.Fail(resource.message!!)))
                Status.NETWORK_ERROR -> {}
            }
        }
    }
}