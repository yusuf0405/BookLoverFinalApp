package com.example.domain.usecase

import com.example.domain.models.book.AddNewBookRequest
import com.example.domain.repository.BookRepository

class AddNewStudentBookUseCase(private var repository: BookRepository) {
    fun execute(
        book: AddNewBookRequest,
    ) = repository.addNewStudentBook(book = book)
}