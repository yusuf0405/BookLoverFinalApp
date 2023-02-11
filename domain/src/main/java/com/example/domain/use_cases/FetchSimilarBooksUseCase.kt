package com.example.domain.use_cases

import com.example.domain.DispatchersProvider
import com.example.domain.models.AudioBookDomain
import com.example.domain.models.BookDomain
import com.example.domain.repository.AudioBooksRepository
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.flow.*

interface FetchSimilarBooksUseCase {

    operator fun invoke(
        bookId: String,
        genresId: List<String>
    ): Flow<Pair<List<BookDomain>, List<AudioBookDomain>>>

}

class FetchSimilarBooksUseCaseImpl(
    private val repository: BooksRepository,
    private val audioBooksRepository: AudioBooksRepository,
    private val dispatchersProvider: DispatchersProvider,
) : FetchSimilarBooksUseCase {

    override fun invoke(
        bookId: String,
        genresId: List<String>
    ): Flow<Pair<List<BookDomain>, List<AudioBookDomain>>> =
        repository.fetchBooksFromCache()
            .combine(audioBooksRepository.fetchAllAudioBooksFromCache()) { books, audioBooks ->
                startFilteredSimilarBooks(
                    genres = genresId,
                    audioBooks = audioBooks,
                    bookId = bookId,
                    books = books
                )
            }
            .flowOn(dispatchersProvider.default())

    private fun startFilteredSimilarBooks(
        genres: List<String>,
        books: List<BookDomain>,
        audioBooks: List<AudioBookDomain>,
        bookId: String
    ): Pair<List<BookDomain>, List<AudioBookDomain>> {
        val similarBooks = mutableListOf<BookDomain>()
        genres.forEach { genre ->
            books.forEach { book ->
                book.genreIds.forEach { newGenres ->
                    if (genre == newGenres && book.id != bookId) similarBooks.add(book)
                }
            }
        }
        val similarAudioBooks = mutableListOf<AudioBookDomain>()
        genres.forEach { genre ->
            audioBooks.forEach { book ->
                book.genres.forEach { newGenres ->
                    if (genre == newGenres && book.id != bookId) similarAudioBooks.add(book)
                }
            }
        }

        return Pair(similarBooks.distinctBy { it.id }, similarAudioBooks.distinctBy { it.id })
    }
}