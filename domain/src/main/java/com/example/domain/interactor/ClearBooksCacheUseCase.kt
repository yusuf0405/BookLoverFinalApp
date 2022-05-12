package com.example.domain.interactor

import com.example.domain.repository.BooksRepository

class ClearBooksCacheUseCase(private val repository: BooksRepository) {

    suspend fun execute() = repository.clearBooksCache()
}