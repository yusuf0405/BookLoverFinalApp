package com.example.domain.domain.interactor

import com.example.domain.domain.repository.BookThatReadRepository

class GetMyStudentBooksUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String) = repository.fetchMyStudentBooks(id = id)
}