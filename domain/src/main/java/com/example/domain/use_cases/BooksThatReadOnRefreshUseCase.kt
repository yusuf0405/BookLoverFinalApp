package com.example.domain.use_cases

import com.example.domain.repository.BookThatReadRepository

class BooksThatReadOnRefreshUseCase(private var repository: BookThatReadRepository) {
    fun execute(id: String) {

    }
}