package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class AddSessionTokenUseCase(private val repository: UserRepository) {
    fun execute(id: String, sessionToken: String) =
        repository.addSessionToken(id = id, sessionToken = sessionToken)
}