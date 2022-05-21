package com.example.domain.interactor

import com.example.domain.repository.BooksRepository

class GetSimilarBooksUseCase(private val repository: BooksRepository) {
    fun execute(genres: List<String>,bookId:String) = repository.fetchSimilarBooks(genres = genres,bookId =bookId)
}