package com.example.domain.domain.interactor

import com.example.domain.domain.models.BookThatReadDomain
import com.example.domain.domain.repository.BookThatReadRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow

class GetMyBookUseCase(private val repository: BookThatReadRepository) {

    fun execute(id: String): Flow<Resource<BookThatReadDomain>> = repository.getMyBook(id = id)

}