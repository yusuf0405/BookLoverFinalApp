package com.example.domain.interactor

import com.example.domain.models.AddBookQuestionDomain
import com.example.domain.repository.BooksRepository

class AddNewBookQuestionUseCase(private val repository: BooksRepository) {
    fun execute(question: AddBookQuestionDomain) = repository.addBookQuestion(question = question)
}