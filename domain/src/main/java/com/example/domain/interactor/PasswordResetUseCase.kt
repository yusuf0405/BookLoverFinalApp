package com.example.domain.interactor

import com.example.domain.repository.LoginRepository

class PasswordResetUseCase(private val repository: LoginRepository) {
    fun execute(email: String) = repository.passwordReset(email = email)
}