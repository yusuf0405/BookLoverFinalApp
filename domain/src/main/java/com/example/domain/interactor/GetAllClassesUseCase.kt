package com.example.domain.interactor

import com.example.domain.repository.SchoolRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetAllClassesUseCase(private val repository: SchoolRepository) {
    fun execute() = repository.getAllClasses().flowOn(Dispatchers.IO)
}