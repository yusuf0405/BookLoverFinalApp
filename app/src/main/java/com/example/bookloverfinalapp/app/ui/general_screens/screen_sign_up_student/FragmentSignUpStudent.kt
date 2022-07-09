package com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up_student

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.models.UserType
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.ui.general_screens.screen_sign_up.FragmentSignUpViewModel
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.FragmentSignUpStudentBinding
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserSignUpDomain
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class FragmentSignUpStudent :
    BaseFragment<FragmentSignUpStudentBinding, FragmentSignUpViewModel>(FragmentSignUpStudentBinding::inflate),
    View.OnClickListener {

    override val viewModel: FragmentSignUpViewModel by viewModels()

    private val classesTitleList = mutableListOf<String>()
    private var classList = mutableListOf<SchoolClass>()
    private var classCurrentIndex = 0
    private var classTitle = ""
    private var classId = ""

    private val schoolTitleList = mutableListOf<String>()
    private var schoolList = mutableListOf<SchoolDomain>()
    private var schoolCurrentIndex = 0
    private var schoolTitle = ""
    private var schoolId = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeRecourse()
    }

    private fun observeRecourse() {
        viewModel.schoolsErrorCollect(viewLifecycleOwner) { value ->
            value.getValue()?.let { message ->
                val listener = DialogInterface.OnClickListener { _, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE) viewModel.getAllSchools()
                }
                requireContext().showErrorDialog(message, listener)
            }
        }

        viewModel.classErrorCollect(viewLifecycleOwner) { value ->
            value.getValue()?.let { message ->
                val listener = DialogInterface.OnClickListener { _, which ->
                    if (which == DialogInterface.BUTTON_POSITIVE)
                        viewModel.getClasses(schoolList[schoolCurrentIndex].objectId)
                }
                requireContext().showErrorDialog(message, listener)
            }
        }

        viewModel.schoolsCollect(viewLifecycleOwner) { schools ->
            schoolList = schools.toMutableList()
            schools.forEach { schoolTitleList.add(it.title) }
            binding().schoolTextView.text = schoolTitleList[schoolCurrentIndex]
            schoolTitle = schoolTitleList[schoolCurrentIndex]
            schoolId = schools[schoolCurrentIndex].objectId
            viewModel.getClasses(schoolList[schoolCurrentIndex].objectId)
        }

        viewModel.classesCollect(viewLifecycleOwner) { classes ->
            classList = classes.toMutableList()
            classesTitleList.clear()
            classCurrentIndex = 0
            classList.forEach { classesTitleList.add(it.title) }
            classTitle = classesTitleList[classCurrentIndex]
            classId = classList[classCurrentIndex].id
            binding().classTextView.text = classTitle

        }
    }

    private fun setOnClickListeners() {
        binding().apply {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            downEffect(signInLink).setOnClickListener(this@FragmentSignUpStudent)
            downEffect(signUpBtn).setOnClickListener(this@FragmentSignUpStudent)
            downEffect(schoolButton).setOnClickListener(this@FragmentSignUpStudent)
            downEffect(classButton).setOnClickListener(this@FragmentSignUpStudent)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding().signUpBtn -> signUp()
            binding().signInLink -> viewModel.goStudentToLoginFragment()
            binding().schoolButton -> showSchoolSingleChoiceWithConfirmationAlertDialog()
            binding().classButton -> showClassSingleChoiceWithConfirmationAlertDialog()
        }
    }

    private fun signUp() {
        binding().apply {
            if (!lastNameField.validateName()) showToast(message = getString(R.string.last_name_input_format_error))
            else if (!firstNameField.validateName()) showToast(message = getString(R.string.name_input_format_error))
            else if (!emailField.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
            else if (!passwordField.validatePassword()) showToast(message = getString(R.string.password_input_format_error))
            else if (!phoneField.validatePhone()) showToast(message = getString(R.string.phone_input_format_error))
            else {
                val gender = if (male.isChecked) "male" else "female"
                val studentDto = UserSignUpDomain(
                    name = firstNameField.text.toString(),
                    lastname = lastNameField.text.toString(),
                    email = emailField.text.toString(),
                    password = passwordField.text.toString(),
                    number = phoneField.text.toString(),
                    className = classTitle,
                    schoolName = schoolTitle,
                    gender = gender,
                    classId = classId,
                    userType = "student",
                    schoolId = schoolId
                )
                viewModel.signUp(user = studentDto).observe(viewLifecycleOwner) { user ->
                    signUpSuccess(
                        id = user.id,
                        gender = gender,
                        createdAt = user.createdAt,
                        sessionToken = user.sessionToken,
                        image = user.image.toDto()
                    )
                }
            }
        }
    }

    private fun signUpSuccess(
        id: String,
        gender: String,
        createdAt: Date,
        sessionToken: String,
        image: UserImage,
    ) {
        binding().apply {
            val currentStudent = User(
                id = id,
                name = firstNameField.text.toString().trim(),
                lastname = lastNameField.text.toString().trim(),
                email = emailField.text.toString().trim(),
                password = passwordField.text.toString(),
                number = phoneField.text.toString().trim(),
                gender = gender,
                className = classTitle,
                schoolName = schoolTitle,
                classId = classId,
                createAt = createdAt,
                userType = UserType.student,
                sessionToken = sessionToken,
                image = image,
                schoolId = schoolId
            )
            viewModel.addSessionToken(id = id, sessionToken = sessionToken).observe(viewLifecycleOwner) {
                    SharedPreferences().saveCurrentUser(user = currentStudent, requireActivity())
                    intentClearTask(activity = ActivityMain())
                }
        }
    }

    private fun showClassSingleChoiceWithConfirmationAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.class_setup)
            .setSingleChoiceItems(classesTitleList.toTypedArray(), classCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                classCurrentIndex = index
                classTitle = classesTitleList[index]
                classId = classList[index].id
                binding().classTextView.text = classTitle
            }
            .create()
        dialog.show()
    }

    private fun showSchoolSingleChoiceWithConfirmationAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.volume_school_setup)
            .setSingleChoiceItems(schoolTitleList.toTypedArray(), schoolCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                if (index != schoolCurrentIndex) viewModel.getClasses(schoolList[index].objectId)
                schoolCurrentIndex = index
                schoolTitle = schoolTitleList[index]
                schoolId = schoolList[index].objectId
                binding().schoolTextView.text = schoolTitle
            }
            .create()
        dialog.show()
    }

}