package com.joseph.sign_in

interface SignInModuleRepository {

    suspend fun signIn(email: String, password: String)
}