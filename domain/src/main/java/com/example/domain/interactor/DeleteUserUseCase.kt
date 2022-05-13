package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class DeleteUserUseCase(private val repository: UserRepository) {
    fun execute(id: String, sessionToken: String) =
        repository.deleteUser(id = id, sessionToken = sessionToken)
}