package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class ClearStudentsCacheUseCase(private val repository: UserRepository) {
    suspend fun execute() = repository.clearStudentsCache()
}