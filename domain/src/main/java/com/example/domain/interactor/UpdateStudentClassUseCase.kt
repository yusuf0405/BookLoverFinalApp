package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class UpdateStudentClassUseCase(private val repository: UserRepository) {
    fun execute(
        id: String,
        sessionToken: String,
        classId: String,
        classTitle: String,
    ) = repository.updateStudentClass(id = id,
        sessionToken = sessionToken,
        classId = classId,
        classTitle = classTitle)
}