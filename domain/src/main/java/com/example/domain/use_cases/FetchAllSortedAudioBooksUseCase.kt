package com.example.domain.use_cases

import com.example.domain.sort_dialog.AudioBookSortOrder
import com.example.domain.models.AudioBookDomain
import com.example.domain.repository.AudioBooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

interface FetchAllSortedAudioBooksUseCase {

    operator fun invoke(
        schoolId: String,
        order: AudioBookSortOrder
    ): Flow<List<AudioBookDomain>>

}

class FetchAllSortedAudioBooksUseCaseImpl(
    private val audioBooksRepository: AudioBooksRepository,
) : FetchAllSortedAudioBooksUseCase {

    override fun invoke(
        schoolId: String,
        order: AudioBookSortOrder
    ): Flow<List<AudioBookDomain>> =
        audioBooksRepository.fetchAllAudioBooks(schoolId = schoolId).map { books ->
            fetchSortedBooksByOrder(order = order, books = books)
        }.flowOn(Dispatchers.Default)

    private fun fetchSortedBooksByOrder(
        order: AudioBookSortOrder,
        books: List<AudioBookDomain>
    ) = when (order) {
        AudioBookSortOrder.BY_DATE -> books.sortedByDescending { it.createdAt }
        AudioBookSortOrder.BY_BOOK_NAME -> books.sortedBy { it.title }
        AudioBookSortOrder.BY_AUTHOR_NAME -> books.sortedBy { it.author }
    }
}