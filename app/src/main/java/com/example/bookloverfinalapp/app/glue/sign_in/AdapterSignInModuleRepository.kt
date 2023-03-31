package com.example.bookloverfinalapp.app.glue.sign_in

import com.example.domain.repository.LoginRepository
import com.joseph.sign_in.SignInModuleRepository
import javax.inject.Inject

class AdapterSignInModuleRepository @Inject constructor(
    private val repository: LoginRepository
) : SignInModuleRepository {

    override suspend fun signIn(email: String, password: String) {
        val dd = repository.signIn(email = email, password = password)
    }
}