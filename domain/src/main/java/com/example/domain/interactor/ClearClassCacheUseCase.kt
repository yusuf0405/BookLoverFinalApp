package com.example.domain.interactor

import com.example.domain.repository.ClassRepository

class ClearClassCacheUseCase(private var repository: ClassRepository) {
    suspend fun execute() = repository.clearTable()
}