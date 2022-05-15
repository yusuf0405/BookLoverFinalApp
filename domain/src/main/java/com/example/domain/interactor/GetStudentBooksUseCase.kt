package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetStudentBooksUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String) = repository.fetchMyStudentBooks(id = id).flowOn(Dispatchers.IO)
}