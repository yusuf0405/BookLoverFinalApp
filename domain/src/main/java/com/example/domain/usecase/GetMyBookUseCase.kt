package com.example.domain.usecase

import com.example.domain.repository.BookRepository

class GetMyBookUseCase(private val repository: BookRepository) {
    fun execute(userId: String, bookId: String) =
        repository.getMyBook(userId = userId, bookId = bookId)
}