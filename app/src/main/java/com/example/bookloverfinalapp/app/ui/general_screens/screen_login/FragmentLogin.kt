package com.example.bookloverfinalapp.app.ui.general_screens.screen_login

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_main.ActivityAdminMain
import com.example.bookloverfinalapp.app.ui.general_screens.ProgressDialog
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.ActivityMain
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.FragmentLoginBinding
import com.joseph.ui.core.custom.snackbar.GenericSnackbar
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.showOnlyOne
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class FragmentLogin :
    BaseFragment<FragmentLoginBinding, FragmentLoginViewModel>(FragmentLoginBinding::inflate) {

    override val viewModel: FragmentLoginViewModel by viewModels()

    private val progressDialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
        ProgressDialog.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeData()
    }

    private fun setOnClickListeners() = with(binding()) {
        signInBtn.setOnDownEffectClickListener { signInWithEmail() }
        blockNoAccount.signUpLink.setOnDownEffectClickListener { viewModel.goOverSignUpFragment() }
        forgotPassword.setOnDownEffectClickListener { viewModel.goForgotPasswordFragment() }
//        toolbar.setOnDownEffectClickListener { viewModel.navigateBack() }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            successSignInFlow.observe { user ->
                user.password = binding().password.text.toString()
                SharedPreferences().saveCurrentUser(user = user, activity = requireActivity())
                if (user.userType == UserType.admin) intentClearTask(activity = ActivityAdminMain())
                else intentClearTask(activity = ActivityMain())
            }
            loadingDialogFlow.filterNotNull().observe(::handleProgressDialogShowing)
        }
    }

    private fun handleProgressDialogShowing(isShow: Boolean) {
        if (isShow) {
            progressDialog.showOnlyOne(requireActivity().supportFragmentManager)
        } else {
            progressDialog.dismiss()
        }
    }

    private fun signInWithEmail() = with(binding()) {
        if (!email.validateEmail()) showErrorSnackbar(
            message = getString(R.string.email_input_format_error),
            input = email
        )
        else if (!password.validatePassword()) showErrorSnackbar(
            message = getString(R.string.password_input_format_error),
            input = password
        )
        else startSignIn()
    }

    private fun showErrorSnackbar(message: String, input: EditText) =
        GenericSnackbar
            .Builder(binding().root)
            .error()
            .message(message)
            .buttonText(getString(R.string.fix))
            .buttonClickListener { input.requestFocus() }
            .build()
            .show()


    private fun startSignIn() {
        val email = binding().email.text.toString()
        val password = binding().password.text.toString()
        viewModel.signInWithEmailUseCase(
            email = email,
            password = password
        )
    }
}