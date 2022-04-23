package com.example.domain.usecase

import com.example.domain.repository.SchoolRepository

class GetAllClassesUseCase(private val repository: SchoolRepository) {
    fun execute() = repository.getAllClasses()
}