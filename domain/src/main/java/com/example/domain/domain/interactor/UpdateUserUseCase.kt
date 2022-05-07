package com.example.domain.domain.interactor

import com.example.domain.models.student.UserUpdateDomain
import com.example.domain.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    fun execute(id: String, user: UserUpdateDomain, sessionToken: String) =
        repository.updateUser(id = id, user = user, sessionToken = sessionToken)
}