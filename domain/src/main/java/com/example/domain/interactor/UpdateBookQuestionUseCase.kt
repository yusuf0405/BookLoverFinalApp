package com.example.domain.interactor

import com.example.domain.models.AddBookQuestionDomain
import com.example.domain.repository.BooksRepository

class UpdateBookQuestionUseCase(private val repository: BooksRepository) {
    fun execute(question: AddBookQuestionDomain, id: String) =
        repository.updateBookQuestion(id = id, question = question)
}