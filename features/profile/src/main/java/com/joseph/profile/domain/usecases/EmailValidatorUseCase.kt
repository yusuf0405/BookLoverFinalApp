package com.joseph.profile.domain.usecases

import com.joseph.common.IdResourceString
import com.joseph.profile.domain.models.*
import com.joseph.profile.domain.models.UserFieldValidatorCorrectState
import com.joseph.profile.domain.models.UserFieldValidatorErrorState
import com.joseph.ui.core.R
import javax.inject.Inject

interface EmailValidatorUseCase {

    operator fun invoke(userEmail: String, currentEmail: String): UserFieldValidatorState

}

class EmailValidatorUseCaseImpl @Inject constructor(
) : EmailValidatorUseCase {

    companion object {
        const val EMAIL_MIN_LENGTH = 6
        const val EMAIL_SYMBOL = "@"
        const val EMAIL_COM_SUFFIX = "gmail.com"
        const val EMAIL_RU_SUFFIX = "mail.ru"
    }

    override fun invoke(
        userEmail: String,
        currentEmail: String
    ): UserFieldValidatorState = when {
        userEmail.isEmpty() -> {
            val defaultText = IdResourceString(R.string.zero)
            UserFieldValidatorEmptyState(defaultText)
        }
        userEmail == currentEmail -> {
            UserFieldValidatorDefaultState(IdResourceString(R.string.zero))
        }
        userEmail.length <= EMAIL_MIN_LENGTH -> {
            val errorText =
                IdResourceString(R.string.the_length_of_the_email_address_must_exceed_n_characters)
            UserFieldValidatorErrorState(errorText)
        }
        !userEmail.contains(EMAIL_SYMBOL) -> {
            val errorText = IdResourceString(R.string.the_email_address_must_contain)
            UserFieldValidatorErrorState(errorText)
        }
        !userEmail.endsWith(EMAIL_COM_SUFFIX) && !userEmail.endsWith(EMAIL_RU_SUFFIX) -> {
            val errorText = IdResourceString(R.string.invalid_format_for_entering_an_email_address)
            UserFieldValidatorErrorState(errorText)
        }
        else -> {
            UserFieldValidatorCorrectState
        }
    }
}