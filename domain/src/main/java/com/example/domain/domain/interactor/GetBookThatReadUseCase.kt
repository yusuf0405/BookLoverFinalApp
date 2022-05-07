package com.example.domain.domain.interactor

import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetBookThatReadUseCase(private val repository: BookThatReadRepository) {
     fun execute(id: String): Flow<Resource<List<BookThatReadDomain>>> =
        repository.fetchMyBooks(id = id).flowOn(
            Dispatchers.IO)
}