package com.example.domain.domain.interactor

import com.example.domain.domain.repository.LoginRepository

class SignInUseCase(private var repository: LoginRepository) {
    fun execute(email: String, password: String) =
        repository.signIn(email = email, password = password)
}