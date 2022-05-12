package com.example.domain.interactor

import com.example.domain.models.UserUpdateDomain
import com.example.domain.repository.UserRepository

class UpdateUserUseCase(private val repository: UserRepository) {
    fun execute(id: String, user: UserUpdateDomain, sessionToken: String) =
        repository.updateUser(id = id, user = user, sessionToken = sessionToken)
}