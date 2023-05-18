package com.joseph.profile.domain.usecases

import com.joseph.common_api.IdResourceString
import com.joseph.profile.domain.models.*
import com.joseph.profile.domain.models.UserFieldValidatorCorrectState
import com.joseph.profile.domain.models.UserFieldValidatorErrorState
import com.joseph.ui_core.R
import javax.inject.Inject

interface PasswordValidatorUseCase {

    operator fun invoke(userPassword: String, currentPassword: String): UserFieldValidatorState

}

class PasswordValidatorUseCaseImpl @Inject constructor(
) : PasswordValidatorUseCase {

    companion object {
        const val PASSWORD_MIN_LENGTH = 8
    }

    override fun invoke(
        userPassword: String,
        currentPassword: String
    ): UserFieldValidatorState = when {
        userPassword == currentPassword -> {
            UserFieldValidatorDefaultState(IdResourceString(R.string.start_changing_your_password))
        }
        userPassword.isEmpty() -> {
            val defaultText = IdResourceString(R.string.set_a_new_password)
            UserFieldValidatorEmptyState(defaultText)
        }
        userPassword.length <= PASSWORD_MIN_LENGTH -> {
            val errorText =
                IdResourceString(R.string.the_password_must_be_longer_than_n_characters)
            UserFieldValidatorErrorState(errorText)
        }
        userPassword.chars().allMatch(Character::isLetter) -> {
            val errorText = IdResourceString(R.string.the_password_must_contain_any_number)
            UserFieldValidatorErrorState(errorText)
        }
        else -> {
            UserFieldValidatorCorrectState
        }
    }
}