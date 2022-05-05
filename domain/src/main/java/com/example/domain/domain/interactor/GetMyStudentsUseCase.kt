package com.example.domain.domain.interactor

import com.example.domain.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class GetMyStudentsUseCase(private val repository: UserRepository) {
    fun execute(className: String, schoolName: String) =
        repository.fetchMyStudents(className = className, schoolName = schoolName).flowOn(Dispatchers.IO)
}