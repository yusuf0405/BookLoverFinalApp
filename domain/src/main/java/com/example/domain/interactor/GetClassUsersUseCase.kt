package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class GetClassUsersUseCase(private val repository: UserRepository) {
    fun execute(classId: String) = repository.fetchClassUsers(classId = classId)
}