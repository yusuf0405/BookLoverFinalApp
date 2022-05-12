package com.example.domain.interactor

import com.example.domain.repository.SchoolRepository

class GetClassUseCase(private val repository: SchoolRepository) {
    fun execute(id: String) = repository.getClass(id = id)
}