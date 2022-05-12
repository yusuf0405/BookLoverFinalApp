package com.example.domain.interactor

import com.example.domain.repository.ClassRepository

class DeleteClassUseCase(private val repository: ClassRepository) {
    fun execute(id: String) = repository.deleteClass(id = id)
}