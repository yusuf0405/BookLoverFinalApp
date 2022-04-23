package com.example.domain.domain.interactor

import com.example.domain.domain.repository.StudentBooksRepository
import com.example.domain.models.Resource
import kotlinx.coroutines.flow.Flow

class DeleteFromMyBooksUseCase(private val repository: StudentBooksRepository) {
    suspend fun execute(id: String): Flow<Resource<Unit>> = repository.deleteMyBook(id = id)
}