package com.example.bookloverfinalapp.app.ui.student_screens.screen_profile

import com.example.bookloverfinalapp.app.base.BaseViewModel

class FragmentStudentProfileViewModel : BaseViewModel() {

    fun goEditProfileFragment() =
        navigate(FragmentStudentProfileDirections.actionFragmentStudentProfileToFragmentStudentEditProfile())
}