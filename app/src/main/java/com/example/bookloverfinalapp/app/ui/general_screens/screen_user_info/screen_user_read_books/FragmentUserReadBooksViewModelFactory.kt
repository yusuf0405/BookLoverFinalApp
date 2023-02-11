package com.example.bookloverfinalapp.app.ui.general_screens.screen_user_info.screen_user_read_books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.data.ResourceProvider
import com.example.domain.DispatchersProvider
import com.example.domain.Mapper
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val USER_ID_KEY = "USER_ID_KEY"

class FragmentUserReadBooksViewModelFactory @AssistedInject constructor(
    @Assisted(USER_ID_KEY) private val userId: String,
    private val userRepository: UserRepository,
    private val bookThatReadRepository: BookThatReadRepository,
    private val dispatchersProvider: DispatchersProvider,
    private val resourceProvider: ResourceProvider,
    private val savedBookDomainToUiMapper: Mapper<BookThatReadDomain, BookThatRead>,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentUserReadBooksViewModel::class.java)
        return FragmentUserReadBooksViewModel(
            userId = userId,
            userRepository = userRepository,
            bookThatReadRepository = bookThatReadRepository,
            savedBookDomainToUiMapper = savedBookDomainToUiMapper,
            dispatchersProvider = dispatchersProvider,
            resourceProvider = resourceProvider
        ) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted(USER_ID_KEY) userId: String,
        ): FragmentUserReadBooksViewModelFactory
    }
}