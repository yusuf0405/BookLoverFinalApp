package com.example.domain.usecase

import com.example.domain.domain.repository.SchoolRepository

class GetAllClassesUseCase(private val repository: SchoolRepository) {
    fun execute() = repository.getAllClasses()
}