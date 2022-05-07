package com.example.domain.domain.interactor

import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class AddNewBookThatReadUseCase(private val repository: BookThatReadRepository) {
    fun execute(book: AddNewBookDomain): Flow<Resource<Unit>> = repository.addBook(book = book).flowOn(
        Dispatchers.IO)
}