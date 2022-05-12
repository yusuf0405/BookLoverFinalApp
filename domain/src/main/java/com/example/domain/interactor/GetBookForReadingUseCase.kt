package com.example.domain.interactor

import com.example.domain.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetBookForReadingUseCase(private val repository: BooksRepository) {
    fun execute(url: String) = repository.getBookForReading(url = url).flowOn(Dispatchers.IO)
}