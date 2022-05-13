package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class GetClassStudentsUseCase(private val repository: UserRepository) {
    fun execute(classId: String) = repository.fetchClassStudents(classId = classId)
}