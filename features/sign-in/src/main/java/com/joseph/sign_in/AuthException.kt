package com.joseph.sign_in

abstract class AuthException(
    val errorMessage: String = String(),
    val throwable: Throwable? = null
) : Throwable()

class ValidateError(errorMessage: String) : AuthException(errorMessage = errorMessage)

class AlreadyRegisteredError() : AuthException(errorMessage = "Такой ")