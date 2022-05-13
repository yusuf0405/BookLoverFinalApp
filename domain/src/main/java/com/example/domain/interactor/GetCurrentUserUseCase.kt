package com.example.domain.interactor

import com.example.domain.repository.UserRepository

class GetCurrentUserUseCase(private val repository: UserRepository) {
    fun execute(sessionToken: String) = repository.getCurrentUser(sessionToken = sessionToken)
}