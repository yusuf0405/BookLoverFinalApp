package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_books

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.mappers.BookDomainToBookModelMapper
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookLoadingModel
import com.example.bookloverfinalapp.app.models.BookModel
import com.example.bookloverfinalapp.app.models.CatEmptyModel
import com.example.bookloverfinalapp.app.models.ErrorModel
import com.example.bookloverfinalapp.app.ui.adapter.ViewHolderChain.Companion.ADAPTER_BOOK_VIEW_HOLDER
import com.example.bookloverfinalapp.app.custom.ItemUi
import com.example.bookloverfinalapp.app.utils.communication.ItemUiCommunication
import com.example.domain.Mapper
import com.example.domain.Resource
import com.example.domain.Status
import com.example.domain.interactor.GetSimilarBooksUseCase
import com.example.domain.models.BookDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class FragmentGenreSimilarBooksViewModel @Inject constructor(
    private val getSimilarBooksUseCase: GetSimilarBooksUseCase,
    private val communication: ItemUiCommunication,
    val adapterMapper: Mapper<BookModel, Book>,
) : BaseViewModel() {

    fun collect(owner: LifecycleOwner, observer: Observer<List<ItemUi>>) =
        communication.observe(owner = owner, observer = observer)

    fun fetchSimilarBook(genre: String) = launchInBackground {
        val genres = mutableListOf<String>()
        genres.add(genre)
        getSimilarBooksUseCase.execute(genres = genres, bookId = "0")
            .collectLatest { resource ->
                fetchResource(resource = resource, genre = genre)
            }
    }

    private fun fetchResource(resource: Resource<List<BookDomain>>, genre: String) {
        when (resource.status) {
            Status.LOADING -> communication.put(listOf(BookLoadingModel))

            Status.SUCCESS -> communication.put(resource.data!!.map { bookDomain ->
                BookDomainToBookModelMapper(ADAPTER_BOOK_VIEW_HOLDER).map(
                    bookDomain)
            })
            Status.EMPTY -> communication.put(listOf(CatEmptyModel))

            Status.ERROR -> communication.put(listOf(ErrorModel(resource.message!!) {
                fetchSimilarBook(genre = genre)
            }))
        }
    }


    fun goBookInfoFragment(book: Book) =
        navigate(FragmentGenreSimilarBooksDirections.actionFragmentGenreSimilarBooksToFragmentBookInfo(
            book = book))

    fun goBack() = navigateBack()

}