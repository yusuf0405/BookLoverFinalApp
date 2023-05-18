package com.joseph.profile.domain.usecases

import com.joseph.common_api.IdResourceString
import com.joseph.profile.domain.models.*
import com.joseph.profile.domain.models.UserFieldValidatorCorrectState
import com.joseph.profile.domain.models.UserFieldValidatorErrorState
import com.joseph.ui_core.R
import javax.inject.Inject

interface NameValidatorUseCase {

    operator fun invoke(userName: String, currentName: String): UserFieldValidatorState

}

class NameValidatorUseCaseImpl @Inject constructor(
) : NameValidatorUseCase {

    companion object {
        const val NAME_MIN_LENGTH = 1
    }

    override fun invoke(
        userName: String,
        currentName: String
    ): UserFieldValidatorState = when {
        userName.isEmpty() -> {
            val defaultText = IdResourceString(R.string.whats_your_name)
            UserFieldValidatorEmptyState(defaultText)
        }
        userName == currentName -> {
            UserFieldValidatorDefaultState(IdResourceString(R.string.start_update_your_name))
        }
        userName.length <= NAME_MIN_LENGTH -> {
            val errorText =
                IdResourceString(R.string.the_name_is_too_short)
            UserFieldValidatorErrorState(errorText)
        }
        !userName.chars().allMatch(Character::isLetter) -> {
            val errorText = IdResourceString(R.string.the_name_cannot_contain_numbers)
            UserFieldValidatorErrorState(errorText)
        }
        else -> {
            UserFieldValidatorCorrectState
        }
    }
}