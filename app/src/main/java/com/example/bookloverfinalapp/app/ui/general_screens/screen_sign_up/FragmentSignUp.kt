package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.databinding.FragmentSignUpBinding

class FragmentSignUp :
    BaseFragment<FragmentSignUpBinding, FragmentSignUpViewModel>(FragmentSignUpBinding::inflate),
    View.OnClickListener {

    override val viewModel: FragmentSignUpViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding().apply {
            studentBtn.setOnClickListener(this@FragmentSignUp)
            teacherBtn.setOnClickListener(this@FragmentSignUp)
            signInLink.setOnClickListener(this@FragmentSignUp)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding().studentBtn -> viewModel.goOverSignUpStudentFragment()
            binding().teacherBtn -> viewModel.goOverSignUpTeacherFragment()
            binding().signInLink -> viewModel.goOverLoginFragment()

        }
    }
}