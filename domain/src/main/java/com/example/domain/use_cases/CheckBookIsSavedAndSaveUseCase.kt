package com.example.domain.use_cases

import com.example.domain.RequestState
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BooksRepository
import com.example.domain.repository.BooksSaveToFileRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream

interface CheckBookIsSavedAndSaveUseCase {

    suspend operator fun invoke(books: List<BookThatReadDomain>)
}

class CheckBookIsSavedAndSaveUseCaseImpl(
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val booksRepository: BooksRepository,
) : CheckBookIsSavedAndSaveUseCase {

    override suspend fun invoke(books: List<BookThatReadDomain>) {
        books.forEach { book ->
            val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(book.objectId)
            if (savedBookPath == null) {
                fetchBookPdfFileForReading(
                    url = book.bookPdfUrl,
                    key = book.bookId,
                    fileName = book.title
                )
            }
        }
    }

    private fun fetchBookPdfFileForReading(url: String, key: String, fileName: String) {
        GlobalScope.launch {
            when (val result = booksRepository.fetchBookPdfFileForReading(url)) {
                is RequestState.Success -> {
                    handleBookSavedToFileAnswer(
                        inputStream = result.data,
                        key = key,
                        fileName = fileName
                    )
                }
                is RequestState.Error -> Unit
            }
        }
    }

    private suspend fun handleBookSavedToFileAnswer(
        inputStream: InputStream,
        key: String,
        fileName: String
    ) {
        booksSaveToFileRepository.saveBookToFile(
            inputStream = inputStream,
            key = key,
            fileName = fileName,
            fileSuffix = PDF_SUFFIX
        )
    }

    private companion object {
        const val PDF_SUFFIX = ".pdf"
    }
}