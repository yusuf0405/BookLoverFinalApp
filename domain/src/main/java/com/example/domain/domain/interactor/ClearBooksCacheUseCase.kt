package com.example.domain.domain.interactor

import com.example.domain.domain.repository.BooksRepository

class ClearBooksCacheUseCase(private val repository: BooksRepository) {

    suspend fun execute() = repository.clearBooksCache()
}