package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.admin_sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_school.FragmentChoiceSchool
import com.joseph.utils_core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentAdminSignUpDialogBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.snackbar.GenericSnackbar
import com.joseph.utils_core.extensions.tuneBottomDialog
import com.joseph.utils_core.extensions.tuneLyricsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAdminSignUpDialog : DialogFragment() {

    private var _binding: FragmentAdminSignUpDialogBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    private val userSignUp = createUserIsAdminType()

    override fun onStart() {
        super.onStart()
        tuneBottomDialog(4)
        tuneLyricsDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminSignUpDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setWindowAnimations(com.joseph.ui_core.R.style.ModalPage_Animation)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        signUpBtn.setOnDownEffectClickListener {
            validateUserValuesAndStartSignUp()
        }
    }

    private fun validateUserValuesAndStartSignUp() = with(binding) {
        when {
            !emailField.validateEmail() -> showErrorSnackbar(
                message
                = getString(R.string.email_input_format_error),
                input = emailField
            )
            !passwordField.validatePassword() -> showErrorSnackbar(
                message
                = getString(R.string.password_input_format_error),
                input = passwordField
            )
            else -> startSignUp()
        }
    }

    private fun startSignUp() {
        userSignUp.email = binding.emailField.text.toString()
        userSignUp.password = binding.passwordField.text.toString()
        showChoiceSchoolModalPage()
    }

    private fun showErrorSnackbar(message: String, input: EditText) =
        GenericSnackbar
            .Builder(binding.root)
            .error()
            .message(message)
            .buttonText(getString(R.string.fix))
            .buttonClickListener { input.requestFocus() }
            .build()
            .show()

    private fun showChoiceSchoolModalPage() {
        dismiss()
        val fragment = FragmentChoiceSchool.newInstance(userSignUp, false)
        ModalPage.Builder()
            .fragment(fragment)
            .title(getString(R.string.to_continue_you_need_to_select_a_school_and_a_class))
            .build()
            .show(requireActivity().supportFragmentManager, ModalPage.TAG)
    }

    private fun createUserIsAdminType() = UserSignUp(
        name = "ADMIN",
        lastname = "ADMIN",
        number = "00000000000",
        gender = String(),
        userType = UserType.admin.name
    )

    companion object {
        fun newInstance() = FragmentAdminSignUpDialog()
    }
}