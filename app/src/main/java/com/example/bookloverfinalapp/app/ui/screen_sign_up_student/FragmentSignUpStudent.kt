package com.example.bookloverfinalapp.app.ui.screen_sign_up_student

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.ui.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.ui.screen_sign_up.FragmentSignUpViewModel
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentSignUpStudentBinding
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.models.student.UserSignUpRes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import java.util.*

@AndroidEntryPoint
class FragmentSignUpStudent :
    BaseFragment<FragmentSignUpStudentBinding, FragmentSignUpViewModel>(
        FragmentSignUpStudentBinding::inflate),
    View.OnClickListener {

    override val viewModel: FragmentSignUpViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val classesTitleList = mutableListOf<String>()
    private var classList = mutableListOf<Class>()
    private var classCurrentIndex = 0
    private var classId = ""
    private var classTitle = ""

    private val schoolTitleList = mutableListOf<String>()
    private var schoolList = mutableListOf<School>()
    private var schoolCurrentIndex = 0


    private var schoolTitle = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeRecourse()
    }

    private fun observeRecourse() {
        viewModel.schools.onEach { schools ->
            schoolList = schools.toMutableList()
            viewModel.getClasses(schoolList[schoolCurrentIndex].classesIds)
            schools.forEach { schoolTitleList.add(it.title) }
            binding().schoolTextView.text = schoolTitleList[0]
            viewModel.getClasses(schoolList[schoolCurrentIndex].classesIds)
        }.launchWhenStarted(lifecycleScope = lifecycleScope)

        viewModel.classes.onEach { classes ->
            classList = classes.toMutableList()
            classesTitleList.clear()
            classCurrentIndex = 0
            classes.forEach { classesTitleList.add(it.title) }
            classTitle = classesTitleList[classCurrentIndex]
            classId = classes[classCurrentIndex].objectId
            binding().classTextView.text = classTitle
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    private fun setOnClickListeners() {
        binding().apply {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            signInLink.setOnClickListener(this@FragmentSignUpStudent)
            signUpBtn.setOnClickListener(this@FragmentSignUpStudent)
            schoolButton.setOnClickListener(this@FragmentSignUpStudent)
            classButton.setOnClickListener(this@FragmentSignUpStudent)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding().signUpBtn -> signUp()
            binding().signInLink -> viewModel.goOverLoginFragment()
            binding().schoolButton -> showSchoolSingleChoiceWithConfirmationAlertDialog(
                schoolTitleList)
            binding().classButton -> showClassSingleChoiceWithConfirmationAlertDialog(list = classesTitleList)

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
                Log.i("classesTitle", classTitle)
                val gender = if (male.isChecked) "male" else "female"
                val studentDto = UserSignUpRes(
                    name = firstNameField.text.toString(),
                    lastname = lastNameField.text.toString(),
                    email = emailField.text.toString(),
                    password = passwordField.text.toString(),
                    number = "+996" + phoneField.text.toString(),
                    className = classTitle,
                    schoolName = schoolTitle,
                    gender = gender,
                    classId = classId,
                    userType = "student"
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
                name = firstNameField.text.toString(),
                lastname = lastNameField.text.toString(),
                email = emailField.text.toString(),
                password = passwordField.text.toString(),
                number = "+996" + phoneField.text.toString(),
                gender = gender,
                className = classTitle,
                schoolName = schoolTitle,
                classId = classId,
                createAt = createdAt,
                userType = UserType.student,
                sessionToken = sessionToken,
                image = image
            )
            CurrentUser().saveCurrentUser(user = currentStudent, activity = requireActivity())
            CheсkNavigation().observeLogin(status = true, activity = requireActivity())
            intentClearTask(activity = ActivityMain())
        }
    }

    private fun showClassSingleChoiceWithConfirmationAlertDialog(list: List<String>) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.volume_class_setup)
            .setSingleChoiceItems(list.toTypedArray(), classCurrentIndex, null)
            .setPositiveButton(R.string.action_confirm) { d, _ ->
                val index = (d as AlertDialog).listView.checkedItemPosition
                classCurrentIndex = index
                classTitle = classesTitleList[index]
                classId = classList[index].objectId
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
                if (index != schoolCurrentIndex) viewModel.getClasses(schoolList[index].classesIds)
                schoolCurrentIndex = index
                schoolTitle = schoolTitleList[index]
                binding().schoolTextView.text = schoolTitle
            }
            .create()
        dialog.show()
    }
}