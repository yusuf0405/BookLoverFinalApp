package com.example.bookloverfinalapp.app.ui.screen_sign_up

import com.example.bookloverfinalapp.app.base.BaseViewModel

class FragmentSignUpViewModel : BaseViewModel() {

    fun goOverSignUpTeacherFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentSignUpTeacher())

    fun goOverSignUpStudentFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentSignUpStudent())

    fun goOverLoginFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentLogin())

    fun goBack() = navigateBack()

}