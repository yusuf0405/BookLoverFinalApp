package com.example.domain.domain.interactor

import com.example.domain.domain.repository.BookThatReadRepository

class ClearBooksThatReadCacheUseCase(private val repository: BookThatReadRepository) {

    suspend fun execute() = repository.clearBooksCache()

}