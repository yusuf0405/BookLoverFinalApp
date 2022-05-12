package com.example.domain.interactor

import com.example.domain.models.UserSignUpDomain
import com.example.domain.repository.LoginRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class SignUpUseCase(private var repository: LoginRepository) {
    fun execute(user: UserSignUpDomain) = repository.signUp(user = user).flowOn(Dispatchers.IO)
}