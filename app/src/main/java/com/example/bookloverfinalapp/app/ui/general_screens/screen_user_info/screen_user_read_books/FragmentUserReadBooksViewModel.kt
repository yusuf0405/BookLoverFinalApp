package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.screen_user_read_books

import androidx.lifecycle.viewModelScope
import com.example.bookloverfinalapp.app.base.BaseViewModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookAdapterModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_saved_books.adapter.SavedBookItemOnClickListeners
import com.example.bookloverfinalapp.app.utils.extensions.createSharedFlowAsSingleLiveEvent
import com.example.data.ResourceProvider
import com.example.data.cache.models.IdResourceString
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.UserRepository
import kotlinx.coroutines.flow.*

class FragmentUserReadBooksViewModel constructor(
    userId: String,
    private val userRepository: UserRepository,
    private val bookThatReadRepository: BookThatReadRepository,
    dispatchersProvider: DispatchersProvider,
    resourceProvider: ResourceProvider,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
) : BaseViewModel(), SavedBookItemOnClickListeners {

    private val userIdFlow = MutableStateFlow(userId)
    val userFlow = userIdFlow.map(userRepository::fetchUserFromId)

    private val _isErrorFlow = createSharedFlowAsSingleLiveEvent<IdResourceString>()
    val isErrorFlow: SharedFlow<IdResourceString> get() = _isErrorFlow.asSharedFlow()

    val userBooksFlow = userIdFlow
        .flatMapLatest(bookThatReadRepository::fetchUserAllBooksThatReadFromCloud)
        .map { savedBooks -> savedBooks.map(savedBookDomainToUiMapper::map) }
        .map { savedBooks -> savedBooks.map { SavedBookAdapterModel(it, listeners = this) } }
        .flowOn(dispatchersProvider.default())
        .catch { exception: Throwable ->
            _isErrorFlow.tryEmit(resourceProvider.fetchIdErrorMessage(exception))
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}