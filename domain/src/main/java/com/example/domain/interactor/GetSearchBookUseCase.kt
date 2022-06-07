package com.example.domain.interactor

import com.example.domain.repository.BooksRepository

class GetSearchBookUseCase(private val repository: BooksRepository) {
    fun execute(schoolId: String, searchText: String) =
        repository.fetchSearchBook(searchText = searchText, schoolId = schoolId)
}