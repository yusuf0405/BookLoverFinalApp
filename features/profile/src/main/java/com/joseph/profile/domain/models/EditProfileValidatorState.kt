package com.joseph.profile.domain.models

import com.joseph.common_api.IdResourceString
import com.joseph.ui_core.R

interface EditProfileValidatorState

enum class ValidatorTextColors {
    DEFAULT,
    ERROR,
    SUCCESS
}

enum class ValidatorTextStyle {
    DEFAULT,
    LITTLE,
}

internal typealias UserFieldValidatorDefaultState = UserFieldValidatorState.UserFieldValidatorDefaultState
internal typealias UserFieldValidatorCorrectState = UserFieldValidatorState.UserFieldValidatorCorrectState
internal typealias UserFieldValidatorErrorState = UserFieldValidatorState.UserFieldValidatorErrorState
internal typealias UserFieldValidatorEmptyState = UserFieldValidatorState.UserFieldValidatorEmptyState

sealed class UserFieldValidatorState(
    private val _message: IdResourceString?,
    private val _textColor: ValidatorTextColors,
    private val _texStyle: ValidatorTextStyle,
) : EditProfileValidatorState {

    val message = _message
    val textColor = _textColor
    val texStyle = _texStyle

    data class UserFieldValidatorErrorState(
        private val errorMessage: IdResourceString
    ) : UserFieldValidatorState(
        _message = errorMessage,
        _textColor = ValidatorTextColors.ERROR,
        _texStyle = ValidatorTextStyle.LITTLE
    )

    object UserFieldValidatorCorrectState : UserFieldValidatorState(
        _message = IdResourceString(R.string.everything_is_correct),
        _textColor = ValidatorTextColors.SUCCESS,
        _texStyle = ValidatorTextStyle.LITTLE
    )

    data class UserFieldValidatorDefaultState(
        private val defaultMessage: IdResourceString
    ) : UserFieldValidatorState(
        _message = defaultMessage,
        _textColor = ValidatorTextColors.DEFAULT,
        _texStyle = ValidatorTextStyle.LITTLE
    )

    data class UserFieldValidatorEmptyState(
        private val emptyMessage: IdResourceString
    ) : UserFieldValidatorState(
        _message = emptyMessage,
        _textColor = ValidatorTextColors.DEFAULT,
        _texStyle = ValidatorTextStyle.DEFAULT
    )

    companion object {

        fun fetchAllUserFieldsIsCorrect(
            emailValidator: UserFieldValidatorState?,
            passwordValidator: UserFieldValidatorState?,
            nameValidator: UserFieldValidatorState?,
            lastNameValidator: UserFieldValidatorState?
        ): Boolean {
            val emailValidatorIsCorrect = emailValidator is UserFieldValidatorCorrectState
            val passwordValidatorIsCorrect = passwordValidator is UserFieldValidatorCorrectState
            val nameValidatorIsCorrect = nameValidator is UserFieldValidatorCorrectState
            val lastNameValidatorIsCorrect = lastNameValidator is UserFieldValidatorCorrectState
            return emailValidatorIsCorrect || passwordValidatorIsCorrect
                    || nameValidatorIsCorrect || lastNameValidatorIsCorrect
        }


        fun fetchUserFieldsIsError(
            emailValidator: UserFieldValidatorState?,
            passwordValidator: UserFieldValidatorState?,
            nameValidator: UserFieldValidatorState?,
            lastNameValidator: UserFieldValidatorState?,
        ): Boolean {
            val emailValidatorIsError = emailValidator is UserFieldValidatorErrorState
            val passwordValidatorIsError = passwordValidator is UserFieldValidatorErrorState
            val nameValidatorIsError = nameValidator is UserFieldValidatorErrorState
            val lastNameValidatorIsError = lastNameValidator is UserFieldValidatorErrorState
            return emailValidatorIsError || passwordValidatorIsError
                    || nameValidatorIsError || lastNameValidatorIsError
        }

        fun fetchAllUserFieldsIsDefault(
            emailValidator: UserFieldValidatorState?,
            passwordValidator: UserFieldValidatorState?,
            nameValidator: UserFieldValidatorState?,
            lastNameValidator: UserFieldValidatorState?,
        ): Boolean {
            val emailValidatorIsDefault =
                emailValidator is UserFieldValidatorDefaultState

            val passwordValidatorIsDefault =
                passwordValidator is UserFieldValidatorDefaultState

            val nameValidatorIsDefault =
                nameValidator is UserFieldValidatorDefaultState

            val lastNameValidatorIsDefault =
                lastNameValidator is UserFieldValidatorDefaultState

            return emailValidatorIsDefault && passwordValidatorIsDefault
                    && nameValidatorIsDefault && lastNameValidatorIsDefault
        }

        fun fetchUserFieldsIsEmpty(
            emailValidator: UserFieldValidatorState?,
            passwordValidator: UserFieldValidatorState?,
            nameValidator: UserFieldValidatorState?,
            lastNameValidator: UserFieldValidatorState?,
        ): Boolean {
            val emailValidatorIsDefault =
                emailValidator is UserFieldValidatorEmptyState

            val passwordValidatorIsDefault =
                passwordValidator is UserFieldValidatorEmptyState

            val nameValidatorIsDefault =
                nameValidator is UserFieldValidatorEmptyState

            val lastNameValidatorIsDefault =
                lastNameValidator is UserFieldValidatorEmptyState

            return emailValidatorIsDefault || passwordValidatorIsDefault
                    || nameValidatorIsDefault || lastNameValidatorIsDefault
        }

    }
}
