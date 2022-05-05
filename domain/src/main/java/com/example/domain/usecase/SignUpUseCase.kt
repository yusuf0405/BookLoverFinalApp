package com.example.domain.usecase

import com.example.domain.models.student.UserSignUpRes
import com.example.domain.domain.repository.LoginRepository

class SignUpUseCase(private var repository: LoginRepository) {
    fun execute(user: UserSignUpRes) = repository.signUp(user = user)
}