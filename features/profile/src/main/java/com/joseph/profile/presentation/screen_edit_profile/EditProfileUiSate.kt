package com.joseph.profile.presentation.screen_edit_profile

import com.joseph.profile.domain.models.EditProfileColorsState
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.domain.models.UserFieldValidatorState

data class EditProfileUiSate(
    val userFeatureModel: UserFeatureModel = UserFeatureModel.unknown(),
    val editProfileColorsState: EditProfileColorsState = EditProfileColorsState.EditProfileDefaultState,
    val emailValidatorState: UserFieldValidatorState? = null,
    val passwordValidatorState: UserFieldValidatorState? = null,
    val nameValidatorState: UserFieldValidatorState? = null,
    val lastNameValidatorState: UserFieldValidatorState? = null,
    val email: String = userFeatureModel.email,
    val password: String = userFeatureModel.password,
    val firstName: String = userFeatureModel.firstName,
    val lastName: String = userFeatureModel.lastName,
    val buttonEnabled: Boolean = false,
    val refreshButtonIsVisible: Boolean = false,
) {

    companion object {
        val preview = EditProfileUiSate(
            userFeatureModel = UserFeatureModel(
                id = String(),
                firstName = "Joseph",
                lastName = "Barbera",
                imageUrl = "https://cdn3.iconfinder.com/data/icons/avatars-9/145/Avatar_Alien-1024.png",
                email = "planetapluton888@gmail.com",
                password = "Joseph123"
            ),
//            editProfileColorsState = EditProfileSuccessState,
            lastNameValidatorState = UserFieldValidatorState.UserFieldValidatorCorrectState,
            nameValidatorState = UserFieldValidatorState.UserFieldValidatorCorrectState,
            passwordValidatorState = UserFieldValidatorState.UserFieldValidatorCorrectState,
            emailValidatorState = UserFieldValidatorState.UserFieldValidatorCorrectState
        )
    }
}