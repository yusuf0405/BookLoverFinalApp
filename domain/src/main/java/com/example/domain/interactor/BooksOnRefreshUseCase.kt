package com.example.domain.interactor

import com.example.domain.repository.BooksRepository

class BooksOnRefreshUseCase(private var repository: BooksRepository) {
    fun execute(schoolId: String) = repository.onRefresh(schoolId = schoolId)
}