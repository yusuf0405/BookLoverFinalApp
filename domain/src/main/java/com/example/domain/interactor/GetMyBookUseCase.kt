package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetMyBookUseCase(private val repository: BookThatReadRepository) {

    fun execute(id: String, userId: String) =
        repository.fetchMyBook(id = id, userId = userId).flowOn(Dispatchers.IO)

}