package com.joseph.sign_in

import javax.inject.Inject

interface SignInUseCase {

    suspend operator fun invoke(
        email: String,
        password: String
    )
}

class SignInUseCaseImpl @Inject constructor(
    private val signInModuleRepository: SignInModuleRepository
) : SignInUseCase {

    override suspend fun invoke(email: String, password: String) {
        if (email.length < 4) {
            throw ValidateError(errorMessage = "Напиши email нормально")
        } else if (password.length < 8) {
            throw ValidateError(errorMessage = "Напиши password нормально")
        } else {
            signInModuleRepository.signIn(email, password)
        }
    }

}
