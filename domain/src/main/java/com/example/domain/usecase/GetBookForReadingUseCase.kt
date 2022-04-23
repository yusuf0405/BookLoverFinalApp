package com.example.domain.usecase

import com.example.domain.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetBookForReadingUseCase(private val repository: BookRepository) {

    fun execute(url: String) = repository.getBookForReading(url = url).flowOn(Dispatchers.IO)
}