package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.UserSignUp
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.choice_school.FragmentChoiceSchool
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.example.bookloverfinalapp.databinding.FragmentSignUpBinding
import com.joseph.ui.core.custom.snackbar.GenericSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentSignUp : BaseFragment<FragmentSignUpBinding, FragmentSignUpViewModel>(
    FragmentSignUpBinding::inflate
) {

    override val viewModel: FragmentSignUpViewModel by viewModels()

    private val userType: UserType by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSignUpArgs.fromBundle(requireArguments()).userType
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() {
        binding().toolbar.title =
            if (userType == UserType.teacher) getString(R.string.teacher_sighUp)
            else getString(R.string.student_signUp)
    }

    private fun setOnClickListeners() = with(binding()) {
        signUpBtn.setOnDownEffectClickListener { validateUserValuesAndStartSignUp() }
        alreadyRegisteredBlock.signInLink.setOnDownEffectClickListener { viewModel.goTeacherToLoginFragment() }
        toolbar.setNavigationOnClickListener { viewModel.goBack() }
    }

    private fun validateUserValuesAndStartSignUp() = with(binding()) {
        when {
            !firstNameField.validateName() ->
                showErrorSnackbar(
                    message = getString(R.string.name_input_format_error),
                    input = firstNameField
                )
            !lastNameField.validateLastName() ->
                showErrorSnackbar(
                    message = getString(R.string.last_name_input_format_error),
                    input = lastNameField
                )
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
            !phoneField.validatePhone() -> showErrorSnackbar(
                message
                = getString(R.string.phone_input_format_error),
                input = phoneField
            )
            else -> startSignUp()
        }
    }

    private fun startSignUp() = with(binding()) {
        val newUser = UserSignUp(
            name = firstNameField.text.toString().trim(),
            lastname = lastNameField.text.toString().trim(),
            email = emailField.text.toString().trim(),
            password = passwordField.text.toString().trim(),
            number = phoneField.text.toString().trim(),
            gender = if (binding().yourGenderBlock.male.isChecked) MALE else FAMALE,
            userType = userType.name,
        )
        showChoiceSchoolModalPage(newUser)
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

    private companion object {
        const val MALE = "male"
        const val FAMALE = "famale"
    }

    private fun showChoiceSchoolModalPage(user: UserSignUp) = ModalPage.Builder()
        .fragment(FragmentChoiceSchool.newInstance(user))
        .title(getString(R.string.to_continue_you_need_to_select_a_school_and_a_class))
        .build()
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)
}