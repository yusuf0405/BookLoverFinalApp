package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository

class BooksThatReadOnRefreshUseCase(private var repository: BookThatReadRepository) {
    fun execute(id: String) = repository.onRefresh(id = id)
}