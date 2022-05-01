package com.example.bookloverfinalapp.app.ui.student_screens.screen_profile

import com.example.bookloverfinalapp.app.base.BaseViewModel

class FragmentProfileViewModel : BaseViewModel() {

    fun goEditProfileFragment() =
        navigate(FragmentProfileDirections.actionFragmentProfileToFragmentEditProfile())
}