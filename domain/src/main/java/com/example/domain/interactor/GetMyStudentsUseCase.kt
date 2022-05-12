package com.example.domain.interactor

import com.example.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetMyStudentsUseCase(private val repository: UserRepository) {
    fun execute(classId: String) = repository.fetchMyStudents(classId = classId).flowOn(Dispatchers.IO)
}