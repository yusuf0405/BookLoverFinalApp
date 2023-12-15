package com.joseph.profile.presentation.screen_edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.joseph.common.base.BaseViewModel
import com.joseph.profile.domain.models.*
import com.joseph.profile.domain.usecases.EmailValidatorUseCase
import com.joseph.profile.domain.usecases.LastNameValidatorUseCase
import com.joseph.profile.domain.usecases.NameValidatorUseCase
import com.joseph.profile.domain.usecases.PasswordValidatorUseCase
import com.joseph.ui.core.extensions.firstLetterCapital
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class EditProfileViewModel @AssistedInject constructor(
    @Assisted private val userFeatureModel: UserFeatureModel,
    private val emailValidatorUseCase: EmailValidatorUseCase,
    private val nameValidatorUseCase: NameValidatorUseCase,
    private val lastNameValidatorUseCase: LastNameValidatorUseCase,
    private val passwordValidatorUseCase: PasswordValidatorUseCase,
) : BaseViewModel() {

    var uiState by mutableStateOf(EditProfileUiSate(userFeatureModel = userFeatureModel))

    private val userEmailFlow = MutableStateFlow(userFeatureModel.email)
    private val userPasswordFlow = MutableStateFlow(userFeatureModel.password)
    private val userNameFlow = MutableStateFlow(userFeatureModel.firstNameLetterCapital())
    private val userLastNameFlow = MutableStateFlow(userFeatureModel.lastNameLetterCapital())

    private val emailValidatorStateFlow = userEmailFlow.map { email ->
        val state = emailValidatorUseCase(email, userFeatureModel.email.lowercase())
        uiState = uiState.copy(emailValidatorState = state)
        checkCurrentEditProfileColor(state)
        state
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val passwordValidatorStateFlow = userPasswordFlow.map { password ->
        val state = passwordValidatorUseCase(password, userFeatureModel.password)
        uiState = uiState.copy(passwordValidatorState = state)
        checkCurrentEditProfileColor(state)
        state
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val nameValidatorStateFlow = userNameFlow.map { name ->
        val state = nameValidatorUseCase(name, userFeatureModel.firstNameLetterCapital())
        uiState = uiState.copy(nameValidatorState = state)
        checkCurrentEditProfileColor(state)
        state
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val lastNameValidatorStateFlow = this.userLastNameFlow.map { lastName ->
        val state = lastNameValidatorUseCase(lastName, userFeatureModel.lastNameLetterCapital())
        uiState = uiState.copy(lastNameValidatorState = state)
        checkCurrentEditProfileColor(state)
        state
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val emailAndPasswordValidatorFlow = emailValidatorStateFlow
        .combine(passwordValidatorStateFlow) { email, password -> email to password }

    private val nameAndLastNameValidatorFlow = nameValidatorStateFlow
        .combine(lastNameValidatorStateFlow) { name, lastName -> name to lastName }

    private val updateButtonEnabledFlow =
        emailAndPasswordValidatorFlow.combine(nameAndLastNameValidatorFlow)
        { emailAndPasswordValidator, nameAndLastNameValidator ->
            handleUserFieldsState(emailAndPasswordValidator, nameAndLastNameValidator)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private fun handleUserFieldsState(
        emailAndPasswordValidator: Pair<UserFieldValidatorState?, UserFieldValidatorState?>,
        nameAndLastNameValidator: Pair<UserFieldValidatorState?, UserFieldValidatorState?>
    ): Boolean {
        val (emailValidator, passwordValidator) = emailAndPasswordValidator
        val (nameValidator, lastNameValidator) = nameAndLastNameValidator

        val userFieldsIsCorrect = UserFieldValidatorState.fetchAllUserFieldsIsCorrect(
            nameValidator = nameValidator,
            emailValidator = emailValidator,
            lastNameValidator = lastNameValidator,
            passwordValidator = passwordValidator
        )

        val userFieldsIsError = UserFieldValidatorState.fetchUserFieldsIsError(
            nameValidator = nameValidator,
            emailValidator = emailValidator,
            lastNameValidator = lastNameValidator,
            passwordValidator = passwordValidator
        )
        val allUserFieldsIsDefault = UserFieldValidatorState.fetchAllUserFieldsIsDefault(
            nameValidator = nameValidator,
            emailValidator = emailValidator,
            lastNameValidator = lastNameValidator,
            passwordValidator = passwordValidator
        )

        val userFieldsIsEmpty = UserFieldValidatorState.fetchUserFieldsIsEmpty(
            nameValidator = nameValidator,
            emailValidator = emailValidator,
            lastNameValidator = lastNameValidator,
            passwordValidator = passwordValidator
        )

        uiState = uiState.copy(
            refreshButtonIsVisible = !allUserFieldsIsDefault,
            buttonEnabled = userFieldsIsCorrect && !userFieldsIsError && !userFieldsIsEmpty
        )
        return userFieldsIsCorrect
    }

    private fun checkCurrentEditProfileColor(state: UserFieldValidatorState) {
        val editProfileColorsState = when (state) {
            is UserFieldValidatorEmptyState -> EditProfileDefaultState
            is UserFieldValidatorErrorState -> EditProfileErrorState
            is UserFieldValidatorCorrectState -> EditProfileSuccessState
            is UserFieldValidatorDefaultState -> EditProfileDefaultState
        }
        uiState = uiState.copy(editProfileColorsState = editProfileColorsState)
    }

    fun updateUserProfile() {
        // TODO:
    }

    fun refreshUserProfile() {
        updateUserEmail(userFeatureModel.email)
        updatePassword(userFeatureModel.password)
        updateUserName(userFeatureModel.firstName)
        updateUserLastName(userFeatureModel.lastName)
    }

    fun updateUserEmail(email: String) {
        val newEmail = email.lowercase()
        uiState = uiState.copy(email = newEmail)
        userEmailFlow.tryEmit(newEmail)
    }

    fun updatePassword(password: String) {
        uiState = uiState.copy(password = password)
        userPasswordFlow.tryEmit(password)
    }

    fun updateUserName(name: String) {
        val newFirstName = name.firstLetterCapital()
        uiState = uiState.copy(firstName = newFirstName)
        userNameFlow.tryEmit(newFirstName)
    }

    fun updateUserLastName(lastName: String) {
        val newLastName = lastName.firstLetterCapital()
        uiState = uiState.copy(lastName = newLastName)
        userLastNameFlow.tryEmit(newLastName)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            userFeatureModel: UserFeatureModel
        ): EditProfileViewModel
    }
}