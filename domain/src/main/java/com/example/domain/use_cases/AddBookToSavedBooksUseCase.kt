package com.example.domain.use_cases

import com.example.domain.RequestState
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.repository.BookThatReadRepository
import com.example.domain.repository.BooksSaveToFileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface AddBookToSavedBooksUseCase {

    suspend operator fun invoke(
        addBook: AddNewBookThatReadDomain,
        pdfUrl: String
    ): Flow<RequestState<Unit>>
}

class AddBookToSavedBooksUseCaseImpl(
    private val bookThatReadRepository: BookThatReadRepository,
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
) : AddBookToSavedBooksUseCase {

    override suspend fun invoke(
        addBook: AddNewBookThatReadDomain,
        pdfUrl: String
    ): Flow<RequestState<Unit>> {
        return booksSaveToFileRepository.saveBookToFile(
            fileUrl = pdfUrl,
            key = addBook.bookId,
            fileName = addBook.title,
        ).map { bookThatReadRepository.addBookToSavedBooks(addBook) }
    }
}