package com.example.domain.domain.interactor

import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow

class GetBookThatReadUseCase(private val repository: BookThatReadRepository) {
    suspend fun execute(id: String): Flow<Resource<List<BookThatReadDomain>>> = repository.fetchMyBooks(id = id)
}