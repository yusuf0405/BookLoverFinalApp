package com.example.bookloverfinalapp.app.ui.student_screens.screen_setting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.databinding.FragmentStudentSettingBinding

class FragmentStudentSetting :
    BaseFragment<FragmentStudentSettingBinding, FragmentStudentSettingViewModel>(
        FragmentStudentSettingBinding::inflate) {
    override val viewModel: FragmentStudentSettingViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}