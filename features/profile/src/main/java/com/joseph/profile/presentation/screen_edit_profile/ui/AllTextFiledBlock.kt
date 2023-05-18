package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.domain.models.UserFieldValidatorState
import com.joseph.profile.presentation.screen_edit_profile.EditProfileUiSate

@Composable
internal fun AllTextFiledBlock(
    editProfileUiSate: EditProfileUiSate,
    onEmailValueChange: (String) -> Unit,
    onNameValueChange: (String) -> Unit,
    onLastNameValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
) {
    val userFeatureModel = editProfileUiSate.userFeatureModel

    var nameValidatorState by remember { mutableStateOf<UserFieldValidatorState?>(null) }
    var nameText by remember { mutableStateOf(userFeatureModel.firstName) }

    var lastNameValidatorState by remember { mutableStateOf<UserFieldValidatorState?>(null) }
    var lastNameText by remember { mutableStateOf(userFeatureModel.lastName) }

    var emailValidatorState by remember { mutableStateOf<UserFieldValidatorState?>(null) }
    var emailText by remember { mutableStateOf(userFeatureModel.email) }

    var passwordValidatorState by remember { mutableStateOf<UserFieldValidatorState?>(null) }
    var passwordText by remember { mutableStateOf(userFeatureModel.password) }

    nameValidatorState = editProfileUiSate.nameValidatorState
    lastNameValidatorState = editProfileUiSate.lastNameValidatorState
    passwordValidatorState = editProfileUiSate.passwordValidatorState
    emailValidatorState = editProfileUiSate.emailValidatorState

    nameText = editProfileUiSate.firstName
    lastNameText = editProfileUiSate.lastName
    passwordText = editProfileUiSate.password
    emailText = editProfileUiSate.email

    nameValidatorState?.let { state ->
        TextFieldEditProfile(
            text = nameText,
            imageVector = Icons.Outlined.Person,
            onValueChange = onNameValueChange,
            userFieldValidatorState = state
        )
    }

    lastNameValidatorState?.let { state ->
        TextFieldEditProfile(
            text = lastNameText,
            imageVector = Icons.Outlined.Person,
            onValueChange = onLastNameValueChange,
            userFieldValidatorState = state
        )
    }

    emailValidatorState?.let { state ->
        TextFieldEditProfile(
            text = emailText,
            imageVector = Icons.Outlined.Email,
            onValueChange = onEmailValueChange,
            userFieldValidatorState = state
        )
    }

    passwordValidatorState?.let { state ->
        TextFieldEditProfile(
            text = passwordText,
            imageVector = Icons.Outlined.Lock,
            onValueChange = onPasswordValueChange,
            userFieldValidatorState = state,
            isPassword = true
        )
    }
}