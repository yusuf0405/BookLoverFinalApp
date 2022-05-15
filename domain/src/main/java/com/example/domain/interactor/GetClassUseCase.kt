package com.example.domain.interactor

import com.example.domain.repository.ClassRepository

class GetClassUseCase(private val repository: ClassRepository) {
    fun execute(id: String) = repository.getClass(id = id)
}