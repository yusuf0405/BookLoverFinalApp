package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository
import com.example.domain.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class DeleteFromMyBooksUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String): Flow<Resource<Unit>> = repository.deleteMyBook(id = id).flowOn(Dispatchers.IO)
}