package com.example.domain.usecase

import com.example.domain.models.book.BookUpdateProgress
import com.example.domain.repository.BookRepository

class UpdateProgressStudentBookUseCase(private val repository: BookRepository) {
    fun execute(id: String, progress: BookUpdateProgress) =
        repository.updateProgressStudentBook(id = id, progress = progress)
}