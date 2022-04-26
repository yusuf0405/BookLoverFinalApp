package com.example.domain.domain.interactor

import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.repository.StudentBooksRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow

class AddNewStudentBookUseCase(private val repository: StudentBooksRepository) {
    fun execute(book: AddNewBookDomain): Flow<Resource<Unit>> = repository.addBook(book = book)
}