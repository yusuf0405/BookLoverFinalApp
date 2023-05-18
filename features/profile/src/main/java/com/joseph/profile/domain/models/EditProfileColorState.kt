package com.joseph.profile.domain.models

enum class EditProfileColors {
    DEFAULT,
    ERROR,
    SUCCESS
}

internal typealias EditProfileDefaultState = EditProfileColorsState.EditProfileDefaultState
internal typealias EditProfileErrorState = EditProfileColorsState.EditProfileErrorState
internal typealias EditProfileSuccessState = EditProfileColorsState.EditProfileSuccessState

sealed class EditProfileColorsState(
    _color: EditProfileColors,
) {
    var color = _color

    object EditProfileDefaultState : EditProfileColorsState(EditProfileColors.DEFAULT)

    object EditProfileErrorState : EditProfileColorsState(EditProfileColors.ERROR)

    object EditProfileSuccessState : EditProfileColorsState(EditProfileColors.SUCCESS)
}
