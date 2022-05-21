package com.example.bookloverfinalapp.app.ui.screen_all_books.ui

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.ui.screen_main_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.communication.BooksAdapterModelCommunication
import com.example.domain.Mapper
import com.example.domain.interactor.GetAllBooksUseCase
import com.example.domain.models.BookDomain
import com.example.domain.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAllBooksViewModel @Inject constructor(
    private val useCaseGetAll: GetAllBooksUseCase,
    private val communication: BooksAdapterModelCommunication,
    private val mapper: Mapper<BookDomain, BookAdapterModel.Base>,
) : BaseViewModel() {

    fun observe(owner: LifecycleOwner, observer: Observer<List<BookAdapterModel>>) =
        communication.observe(owner = owner, observer = observer)

    fun goStudentBookDetailsFragment(book: Book) =
        navigate(FragmentRootStudentBookDirections.actionFragmentRootBookToFragmentBookInfo(book = book))

    fun fetchBooks(schoolId:String) = dispatchers.launchInBackground(viewModelScope) {
        useCaseGetAll.execute(schoolId = schoolId).collectLatest { resource ->
            when (resource.status) {
                Status.LOADING -> communication.put(listOf(BookAdapterModel.Progress))
                Status.SUCCESS -> communication.put(resource.data!!.map { bookDomain -> mapper.map(bookDomain) })
                Status.EMPTY -> communication.put(listOf(BookAdapterModel.Empty))
                Status.ERROR -> communication.put(listOf(BookAdapterModel.Fail(resource.message!!)))
            }
        }
    }
}