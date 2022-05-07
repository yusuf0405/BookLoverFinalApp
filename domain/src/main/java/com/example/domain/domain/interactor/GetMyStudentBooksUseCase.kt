package com.example.domain.domain.interactor

import com.example.domain.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetMyStudentBooksUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String) = repository.fetchMyStudentBooks(id = id).flowOn(Dispatchers.IO)
}