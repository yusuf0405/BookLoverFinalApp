package com.example.domain.domain.interactor

import com.example.domain.domain.models.BookDomain
import com.example.domain.domain.repository.BooksRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetAllBooksUseCase(private val repository: BooksRepository) {
    suspend fun execute(): Flow<Resource<List<BookDomain>>> = repository.fetchBooks().flowOn(Dispatchers.IO)
}