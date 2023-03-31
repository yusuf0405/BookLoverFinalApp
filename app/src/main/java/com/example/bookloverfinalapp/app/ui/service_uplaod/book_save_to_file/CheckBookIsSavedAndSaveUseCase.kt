package com.example.bookloverfinalapp.app.ui.service_uplaod.book_save_to_file

import com.example.bookloverfinalapp.app.App
import com.example.domain.repository.BooksSaveToFileRepository
import com.example.domain.models.BookThatReadDomain
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Singleton

interface CheckBookIsSavedAndSaveUseCase {

    suspend operator fun invoke(books: List<BookThatReadDomain>)
}

@Singleton
class CheckBookIsSavedAndSaveUseCaseImpl(
    private val booksSaveToFileRepository: BooksSaveToFileRepository,
    private val booksRepository: BooksRepository,
) : CheckBookIsSavedAndSaveUseCase {

    override suspend fun invoke(books: List<BookThatReadDomain>) {
        books.forEach { book ->
            val savedBookPath = booksSaveToFileRepository.fetchSavedBookFilePath(book.bookId)
            if (savedBookPath != null) return
                fetchBookPdfFileForReading(
                    url = book.bookPdfUrl,
                    key = book.bookId,
                    fileName = book.title,
                )

        }
    }

    private fun fetchBookPdfFileForReading(
        url: String,
        key: String,
        fileName: String,
    ) {
        GlobalScope.launch {
            booksSaveToFileRepository.saveBookToFile(
                fileUrl = url,
                key = key,
                fileName = fileName,
            ).launchIn(App.applicationScope)
        }
    }
}