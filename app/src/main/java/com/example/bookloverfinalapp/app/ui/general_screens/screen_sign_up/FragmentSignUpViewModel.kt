package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import com.example.bookloverfinalapp.app.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FragmentSignUpViewModel @Inject constructor(

) : BaseViewModel() {

    fun goTeacherToLoginFragment() =
        navigate(FragmentSignUpDirections.actionFragmentSignUpToFragmentLogin())

    fun goBack() = navigateBack()
}