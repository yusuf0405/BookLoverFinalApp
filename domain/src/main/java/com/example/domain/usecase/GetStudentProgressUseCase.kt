package com.example.domain.usecase

import com.example.domain.repository.BookRepository

class GetStudentProgressUseCase(private val repository: BookRepository) {
    fun execute(id: String) = repository.getMyProgress(id = id)
}