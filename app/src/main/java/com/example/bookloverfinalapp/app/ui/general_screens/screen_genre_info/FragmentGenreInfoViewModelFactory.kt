package com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.mappers.GenreItemsToAdapterModelMapper
import com.example.bookloverfinalapp.app.ui.general_screens.screen_genre_info.router.FragmentGenreInfoRouter
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.domain.repository.GenresRepository
import com.example.domain.use_cases.FetchSimilarBooksUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private const val GENRE_ID_KEY = "GENRE_ID_KEY"

class FragmentGenreInfoViewModelFactory @AssistedInject constructor(
    @Assisted(GENRE_ID_KEY) private val genreId: String,
    private val fetchSimilarBooksUseCase: FetchSimilarBooksUseCase,
    private val genresRepository: GenresRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val genreItemsToAdapterModelMapper: GenreItemsToAdapterModelMapper,
    private val router: FragmentGenreInfoRouter,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == FragmentGenreInfoViewModel::class.java)
        return FragmentGenreInfoViewModel(
            genreId = genreId,
            fetchSimilarBooksUseCase = fetchSimilarBooksUseCase,
            genresRepository = genresRepository,
            booksSaveToFileRepository = booksSaveToFileRepository,
            genreItemsToAdapterModelMapper = genreItemsToAdapterModelMapper,
            router = router
        ) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted(GENRE_ID_KEY) genreId: String
        ): FragmentGenreInfoViewModelFactory
    }
}