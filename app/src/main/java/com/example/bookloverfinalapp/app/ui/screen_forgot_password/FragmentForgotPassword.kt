package com.example.bookloverfinalapp.app.ui.screen_forgot_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.validateEmail
import com.example.bookloverfinalapp.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentForgotPassword :
    BaseFragment<FragmentForgotPasswordBinding, FragmentForgotPasswordViewModel>(
        FragmentForgotPasswordBinding::inflate) {

    override val viewModel: FragmentForgotPasswordViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding().apply {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }

            sendMessage.setOnClickListener {
                if (!emailField.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
                else viewModel.passwordReset(email = emailField.text.toString())
                    .observe(viewLifecycleOwner) {
                        showToast(R.string.message_has_been_successfully)
                        viewModel.goBack()
                    }
            }
        }
    }

}