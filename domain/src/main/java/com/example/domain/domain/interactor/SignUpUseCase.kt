package com.example.domain.domain.interactor

import com.example.domain.models.student.UserSignUpDomain
import com.example.domain.domain.repository.LoginRepository

class SignUpUseCase(private var repository: LoginRepository) {
    fun execute(user: UserSignUpDomain) = repository.signUp(user = user)
}