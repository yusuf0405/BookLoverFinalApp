package com.example.domain.interactor

import com.example.domain.models.UpdateBookDomain
import com.example.domain.repository.BooksRepository

class UpdateBookUseCase(private val repository: BooksRepository) {
    fun execute(id: String, book: UpdateBookDomain) = repository.updateBook(id = id, book = book)
}