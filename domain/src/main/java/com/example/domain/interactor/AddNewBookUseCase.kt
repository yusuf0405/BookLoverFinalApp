package com.example.domain.interactor

import com.example.domain.models.AddNewBookDomain
import com.example.domain.repository.BooksRepository

class AddNewBookUseCase(private val repository: BooksRepository) {
    fun execute(book: AddNewBookDomain) = repository.addNewBook(book = book)
}