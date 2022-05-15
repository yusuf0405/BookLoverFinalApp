package com.example.bookloverfinalapp.app.ui.screen_sign_up_teacher

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.ui.screen_sign_up.FragmentSignUpViewModel
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentSignUpTeacherBinding
import com.example.domain.models.SchoolDomain
import com.example.domain.models.UserDomainImage
import com.example.domain.models.UserSignUpDomain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class FragmentSignUpTeacher :
    BaseFragment<FragmentSignUpTeacherBinding, FragmentSignUpViewModel>(
        FragmentSignUpTeacherBinding::inflate),
    View.OnClickListener {

    override val viewModel: FragmentSignUpViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val classTitleList = mutableListOf<String>()
    private var classList = mutableListOf<SchoolClass>()
    private var classCurrentIndex = 0
    private var classId = ""
    private var classTitle = ""

    private val schoolsTitleList = mutableListOf<String>()
    private var schoolList = mutableListOf<SchoolDomain>()
    private var schoolCurrentIndex = 0
    private var schoolId = ""
    private var schoolTitle = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeRecourse()
    }

    private fun setOnClickListeners() {
        binding().apply {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
            signUpBtn.setOnClickListener(this@FragmentSignUpTeacher)
            signInLink.setOnClickListener(this@FragmentSignUpTeacher)
            schoolButton.setOnClickListener(this@FragmentSignUpTeacher)
            classButton.setOnClickListener(this@FragmentSignUpTeacher)
        }
    }

    private fun observeRecourse() {

        viewModel.schoolError.onEach { value ->
            value.getValue()?.let { message ->
                val listener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> viewModel.getAllSchools()
                    }
                }
                requireContext().shoErrorDialog(message, listener)
            }

        }.launchWhenStarted(lifecycleScope = lifecycleScope)

        viewModel.classError.onEach { value ->
            value.getValue()?.let { message ->
                val listener = DialogInterface.OnClickListener { _, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> viewModel.getClasses(schoolList[schoolCurrentIndex].objectId)
                    }
                }
                requireContext().shoErrorDialog(message, listener)
            }

        }.launchWhenStarted(lifecycleScope = lifecycleScope)
        viewModel.schools.onEach { schools ->
            schoolList = schools.toMutableList()
            schools.forEach { schoolsTitleList.add(it.title) }
            schoolTitle = schoolsTitleList[schoolCurrentIndex]
            binding().schoolTextView.text = schoolsTitleList[schoolCurrentIndex]
            schoolTitle = schoolsTitleList[schoolCurrentIndex]
            schoolId = schools[schoolCurrentIndex].objectId
            viewModel.getClasses(schoolList[schoolCurrentIndex].objectId)
        }.launchWhenStarted(lifecycleScope = lifecycleScope)

        viewModel.classes.onEach { classes ->
            classList = classes.toMutableList()
            classTitleList.clear()
            classCurrentIndex = 0
            classes.forEach { classTitleList.add(it.title) }
            classTitle = classTitleList[classCurrentIndex]
            classId = classes[classCurrentIndex].id
            binding().classTextView.text = classTitle
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    override fun onClick(view: View) {
        when (view) {
            binding().signUpBtn -> signUp()
            binding().signInLink -> viewModel.goTeacherToLoginFragment()
            binding().schoolButton -> showSchoolSingleChoiceWithConfirmationAlertDialog(
                schoolsTitleList)
            binding().classButton -> showClassSingleChoiceWithConfirmationAlertDialog(list = classTitleList)

        }
    }

    private fun signUp() {
        binding().apply {
            if (!lastNameField.validateLastName()) showToast(message = getString(R.string.last_name_input_format_error))
            else if (!firstNameField.validateName()) showToast(message = getString(R.string.name_input_format_error))
            else if (!emailField.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
            else if (!passwordField.validatePassword()) showToast(message = getString(R.string.password_input_format_error))
            else if (!phoneField.validatePhone()) showToast(message = getString(R.string.phone_input_format_error))
            else {
                val teacherDto = UserSignUpDomain(
                    name = firstNameField.text.toString().trim(),
                    lastname = lastNameField.text.toString().trim(),
                    email = emailField.text.toString().trim(),
                    password = passwordField.text.toString().trim(),
                    number = "+996" + phoneField.text.toString().trim(),
                    className = classTitle,
                    schoolName = schoolTitle,
                    gender = "female",
                    classId = classId,
                    userType = "teacher",
                    schoolId = schoolId
                )
                viewModel.signUp(user = teacherDto).observe(viewLifecycleOwner) { user ->
                    successSignUp(id = user.id,
                        createdAt = user.createdAt,
                        sessionToken = user.sessionToken,
                        image = user.image
                    )
                }
            }
        }
    }

    private fun successSignUp(
        id: String,
        createdAt: Date,
        sessionToken: String,
        image: UserDomainImage,
    ) {
        binding().apply {
            val currentUser = User(
                id = id,
                name = firstNameField.text.toString(),
                lastname = lastNameField.text.toString(),
                email = emailField.text.toString(),
                password = passwordField.text.toString(),
                number = "+996" + phoneField.text.toString(),
                gender = "female",
                className = classTitle,
                schoolName = schoolTitle,
                classId = classId,
                createAt = createdAt,
                userType = UserType.teacher,
                sessionToken = sessionToken,
                image = image.toDto(),
                schoolId = schoolId
            )
            viewModel.addSessionToken(id = id, sessionToken = sessionToken)
                .observe(viewLifecycleOwner) {
                    CurrentUser().saveCurrentUser(user = currentUser, activity = requireActivity())
                    CheсkNavigation().observeLogin(status = true, activity = requireActivity())
                    intentClearTask(activity = ActivityMain())
                }
        }

    }

    private fun showClassSingleChoiceWithConfirmationAlertDialog(list: List<String>) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.class_setup)
            .setSingleChoiceItems(list.toTypedArray(), classCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                classCurrentIndex = index
                classTitle = classTitleList[index]
                classId = classList[index].id
                binding().classTextView.text = classTitle
            }
            .create()
        dialog.show()
    }

    private fun showSchoolSingleChoiceWithConfirmationAlertDialog(list: List<String>) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.volume_school_setup)
            .setSingleChoiceItems(list.toTypedArray(), schoolCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                if (index != schoolCurrentIndex) viewModel.getClasses(schoolList[index].objectId)
                schoolCurrentIndex = index
                schoolTitle = schoolsTitleList[index]
                schoolId = schoolList[index].objectId
                binding().schoolTextView.text = schoolTitle
            }
            .create()
        dialog.show()
    }
}