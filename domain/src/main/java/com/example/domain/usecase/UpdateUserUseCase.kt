package com.example.domain.usecase

import com.example.domain.models.student.StudentUpdateRes
import com.example.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    fun execute(id: String, student: StudentUpdateRes, sessionToken: String) =
        repository.updateUser(id = id, student = student, sessionToken = sessionToken)
}