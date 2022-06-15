package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_books

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.BookDomainToBookModelMapper
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.ui.adapter.BookLoadingModel
import com.example.bookloverfinalapp.app.ui.adapter.BookModel
import com.example.bookloverfinalapp.app.ui.adapter.CatEmptyModel
import com.example.bookloverfinalapp.app.ui.adapter.ErrorModel
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main_root.FragmentRootStudentBookDirections
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.BooksOnRefreshUseCase
import com.example.domain.interactor.GetAllBooksUseCase
import com.example.domain.interactor.GetSearchBookUseCase
import com.example.domain.models.BookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentAllBooksViewModel @Inject constructor(
    private val useCaseGetAll: GetAllBooksUseCase,
    private val onRefreshUseCase: BooksOnRefreshUseCase,
    private val getSearchBookUseCase: GetSearchBookUseCase,
    private val communication: ItemUiCommunication,
    var mapper: Mapper<BookModel, Book>,
) : BaseViewModel() {


    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchBooks(schoolId: String) = launchInBackground {
        useCaseGetAll.execute(schoolId = schoolId)
            .collectLatest { resource ->
                unravelingResource(resource = resource,
                    schoolId = schoolId)
            }
    }

    fun onRefresh(schoolId: String) = launchInBackground {
        onRefreshUseCase.execute(schoolId = schoolId).collectLatest { resource ->
            unravelingResource(resource = resource, schoolId = schoolId)
        }
    }

    fun searchBook(searchText: String, schoolId: String) = launchInBackground {
        getSearchBookUseCase.execute(schoolId = schoolId, searchText = searchText)
            .collectLatest { resource ->
                unravelingResource(resource = resource, schoolId = schoolId)
            }
    }

    private fun unravelingResource(resource: Resource<List<BookDomain>>, schoolId: String) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(BookLoadingModel))

            Status.SUCCESS -> communication.put(resource.data!!.map { bookDomain ->
                BookDomainToBookModelMapper(ADAPTER_BOOK_VIEW_HOLDER).map(bookDomain)
            })
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))

            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!) {
                onRefresh(schoolId = schoolId)
            }))
        }
    }

    fun goStudentBookDetailsFragment(book: Book) =
        navigate(FragmentRootStudentBookDirections.actionFragmentRootBookToFragmentBookInfo(book = book))

}
