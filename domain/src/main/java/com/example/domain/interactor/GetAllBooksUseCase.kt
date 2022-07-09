package com.example.domain.interactor

import com.example.domain.Resource
import com.example.domain.models.BookDomain
import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class GetAllBooksUseCase(private val repository: BooksRepository) {
    fun execute(schoolId: String): Flow<Resource<List<BookDomain>>> =
        repository.fetchBooks(schoolId = schoolId).flowOn(Dispatchers.IO)
}