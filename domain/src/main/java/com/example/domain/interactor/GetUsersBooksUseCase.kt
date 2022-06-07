package com.example.domain.interactor

import com.example.domain.repository.BookThatReadRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetUsersBooksUseCase(private val repository: BookThatReadRepository) {
    fun execute(id: String) = repository.fetchUsersBooks(id = id).flowOn(Dispatchers.IO)
}