package com.joseph.profile.domain.usecases

import com.joseph.common.IdResourceString
import com.joseph.profile.domain.models.*
import com.joseph.profile.domain.models.UserFieldValidatorCorrectState
import com.joseph.profile.domain.models.UserFieldValidatorErrorState
import com.joseph.ui.core.R
import javax.inject.Inject

interface LastNameValidatorUseCase {

    operator fun invoke(userLastName: String, currentLastName: String): UserFieldValidatorState

}

class LastNameValidatorUseCaseImpl @Inject constructor(
) : LastNameValidatorUseCase {

    companion object {
        const val NAME_MIN_LENGTH = 1
    }

    override fun invoke(
        userLastName: String,
        currentLastName: String
    ): UserFieldValidatorState = when {
        userLastName.isEmpty() -> {
            val defaultText = IdResourceString(R.string.zero)
            UserFieldValidatorEmptyState(defaultText)
        }
        userLastName == currentLastName -> {
            UserFieldValidatorDefaultState(IdResourceString(R.string.zero))
        }
        userLastName.length <= NAME_MIN_LENGTH -> {
            val errorText =
                IdResourceString(R.string.the_last_name_is_too_short)
            UserFieldValidatorErrorState(errorText)
        }
        !userLastName.chars().allMatch(Character::isLetter) -> {
            val errorText = IdResourceString(R.string.the_last_name_cannot_contain_numbers)
            UserFieldValidatorErrorState(errorText)
        }
        else -> {
            UserFieldValidatorCorrectState
        }
    }
}