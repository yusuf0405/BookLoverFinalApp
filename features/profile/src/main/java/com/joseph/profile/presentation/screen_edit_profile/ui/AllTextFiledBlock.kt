package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.joseph.profile.domain.models.UserFieldValidatorState
import com.joseph.profile.presentation.screen_edit_profile.EditProfileUiSate
import com.joseph.ui.core.MyApplicationTheme
import com.joseph.ui.core.R


@Preview
@Composable
fun EditProfileScreenPreview() {
    MyApplicationTheme() {
        AllTextFiledBlock(
            editProfileUiSate = EditProfileUiSate()
        )

    }
}

@Composable
internal fun AllTextFiledBlock(
    editProfileUiSate: EditProfileUiSate,
    onEmailValueChange: (String) -> Unit = {},
    onNameValueChange: (String) -> Unit = {},
    onLastNameValueChange: (String) -> Unit = {},
    onPasswordValueChange: (String) -> Unit = {},
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
            userFieldValidatorState = state,
            onValueChange = onNameValueChange,
            painter = painterResource(id = R.drawable.ic_account),
        )
//        TextFieldEditProfile(
//            text = nameText,
//            imageVector = painterResource(id = R.drawable.ic_account),
//            onValueChange = onNameValueChange,
//            userFieldValidatorState = state
//        )
    }

    lastNameValidatorState?.let { state ->
        TextFieldEditProfile(
            text = lastNameText,
            painter = painterResource(id = R.drawable.ic_account),
            onValueChange = onLastNameValueChange,
            userFieldValidatorState = state
        )
    }

    emailValidatorState?.let { state ->
        TextFieldEditProfile(
            text = emailText,
            painter = painterResource(id = R.drawable.ic_mail),
            onValueChange = onEmailValueChange,
            userFieldValidatorState = state
        )
    }

    passwordValidatorState?.let { state ->
        TextFieldEditProfile(
            text = passwordText,
            painter = painterResource(id = R.drawable.ic_lock),
            onValueChange = onPasswordValueChange,
            userFieldValidatorState = state,
            isPassword = true
        )
    }

    var phoneNumber by rememberSaveable { mutableStateOf("") }
    PhoneField(
        phoneNumber,
        maskNumber = '0',
        onPhoneChanged = { phoneNumber = it })
}
