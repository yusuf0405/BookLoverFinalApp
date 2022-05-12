package com.example.domain.interactor

import com.example.domain.repository.ClassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetAllClassUseCase(private val repository: ClassRepository) {
    fun execute(schoolId: String) =
        repository.fetchAllClass(schoolId = schoolId).flowOn(Dispatchers.IO)
}