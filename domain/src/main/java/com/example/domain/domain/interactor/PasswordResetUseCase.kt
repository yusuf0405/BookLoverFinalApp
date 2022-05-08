package com.example.domain.domain.interactor

import com.example.domain.domain.repository.LoginRepository

class PasswordResetUseCase(private val repository: LoginRepository) {
    fun execute(email: String) = repository.passwordReset(email = email)
}