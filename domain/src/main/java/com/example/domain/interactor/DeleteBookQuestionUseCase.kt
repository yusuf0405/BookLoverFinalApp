package com.example.domain.interactor

import com.example.domain.repository.BooksRepository

class DeleteBookQuestionUseCase(private val repository: BooksRepository) {
    fun execute(id: String) = repository.deleteBookQuestion(id = id)
}