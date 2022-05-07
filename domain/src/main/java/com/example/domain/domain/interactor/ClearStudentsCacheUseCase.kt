package com.example.domain.domain.interactor

import com.example.domain.domain.repository.UserRepository

class ClearStudentsCacheUseCase(private val repository: UserRepository) {
    suspend fun execute() = repository.clearStudentsCache()
}