package com.example.bookloverfinalapp.app.ui.general_screens.screen_forgot_password

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.extensions.validateEmail
import com.example.bookloverfinalapp.databinding.FragmentForgotPasswordBinding
import com.joseph.ui.core.custom.snackbar.GenericSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentForgotPassword :
    BaseFragment<FragmentForgotPasswordBinding, FragmentForgotPasswordViewModel>(
        FragmentForgotPasswordBinding::inflate
    ) {

    override val viewModel: FragmentForgotPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        toolbar.setNavigationOnClickListener { viewModel.navigateBack() }
        sendMessage.setOnClickListener { checkEmailValidateAndStartResetPassword() }
    }

    private fun checkEmailValidateAndStartResetPassword() = with(binding()) {
        if (!emailField.validateEmail()) {
            showErrorSnackbar()
        } else {
            viewModel.passwordReset(email = emailField.text.toString())
//                .observe(viewLifecycleOwner) {
//                    showSuccessSnackbar()
//                    viewModel.navigateBack()
//                }
        }
    }

    private fun showSuccessSnackbar() =
        GenericSnackbar
            .Builder(binding().root)
            .success()
            .message(getString(R.string.message_has_been_successfully))
            .build()
            .show()

    private fun showErrorSnackbar() =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .message(getString(R.string.email_input_format_error))
            .buttonText(getString(R.string.fix))
            .buttonClickListener { binding().emailField.requestFocus() }
            .build()
            .show()
}