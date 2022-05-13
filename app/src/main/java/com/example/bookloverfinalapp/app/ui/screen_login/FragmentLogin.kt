package com.example.bookloverfinalapp.app.ui.screen_login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main.ActivityAdminMain
import com.example.bookloverfinalapp.app.ui.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.extensions.intentClearTask
import com.example.bookloverfinalapp.app.utils.extensions.showToast
import com.example.bookloverfinalapp.app.utils.extensions.validateEmail
import com.example.bookloverfinalapp.app.utils.extensions.validatePassword
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLogin :
    BaseFragment<FragmentLoginBinding, FragmentLoginViewModel>(FragmentLoginBinding::inflate),
    View.OnClickListener {

    override val viewModel: FragmentLoginViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding().apply {
            signInBtn.setOnClickListener(this@FragmentLogin)
            signUpLink.setOnClickListener(this@FragmentLogin)
            forgotPassword.setOnClickListener(this@FragmentLogin)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding().signInBtn -> signInWithEmail()
            binding().signUpLink -> viewModel.goOverSignUpFragment()
            binding().forgotPassword -> viewModel.goForgotPasswordFragment()
        }
    }

    private fun signInWithEmail() {
        binding().apply {
            if (!email.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
            else if (!password.validatePassword()) showToast(message = getString(R.string.password_input_format_error))
            else signIn()
        }
    }

    private fun signIn() {
        val email = binding().email.text.toString()
        val password = binding().password.text.toString()
        viewModel.signInWithEmailUseCase(
            email = email,
            password = password).observe(viewLifecycleOwner) { user ->
            user.password = password
            CurrentUser().saveCurrentUser(user = user, activity = requireActivity())
            if (binding().reminder.isChecked) CheсkNavigation().observeLogin(status = true,
                activity = requireActivity())
            if (user.userType == UserType.admin) intentClearTask(activity = ActivityAdminMain())
            else intentClearTask(activity = ActivityMain())
        }
    }
}