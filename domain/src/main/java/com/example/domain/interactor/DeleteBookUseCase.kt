package com.example.domain.interactor

import com.example.domain.repository.BooksRepository

class DeleteBookUseCase(private val repository: BooksRepository) {
    fun execute(id: String) = repository.deleteBook(id = id)
}