package com.example.bookloverfinalapp.app.ui.screen_sign_up_teacher

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.ui.screen_main.ActivityMain
import com.example.bookloverfinalapp.app.ui.screen_sign_up.FragmentSignUpViewModel
import com.example.bookloverfinalapp.app.utils.UserType
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentSignUpTeacherBinding
import com.example.domain.domain.models.UserDomainImage
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.models.student.UserSignUpRes
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
    private var classList = mutableListOf<Class>()
    private var classCurrentIndex = 0
    private var classId = ""
    private var classTitle = ""

    private val schoolsTitleList = mutableListOf<String>()
    private var schoolsList = mutableListOf<School>()
    private var schoolCurrentIndex = 0


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
        viewModel.schools.onEach { schools ->
            schoolsList = schools.toMutableList()
            viewModel.getClasses(schoolsList[schoolCurrentIndex].classesIds)
            schools.forEach { schoolsTitleList.add(it.title) }
            binding().schoolTextView.text = schoolsTitleList[0]
            viewModel.getClasses(schoolsList[schoolCurrentIndex].classesIds)
        }.launchWhenStarted(lifecycleScope = lifecycleScope)

        viewModel.classes.onEach { classes ->
            classList = classes.toMutableList()
            classTitleList.clear()
            classCurrentIndex = 0
            classes.forEach { classTitleList.add(it.title) }
            classTitle = classTitleList[classCurrentIndex]
            classId = classes[classCurrentIndex].objectId
            binding().classTextView.text = classTitle
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    override fun onClick(view: View) {
        when (view) {
            binding().signUpBtn -> signUp()
            binding().signInLink -> viewModel.goOverLoginFragment()
            binding().schoolButton -> showSchoolSingleChoiceWithConfirmationAlertDialog(schoolsTitleList)
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
                val teacherDto = UserSignUpRes(
                    name = firstNameField.text.toString(),
                    lastname = lastNameField.text.toString(),
                    email = emailField.text.toString(),
                    password = passwordField.text.toString(),
                    number = "+996" + phoneField.text.toString(),
                    className = classTitle,
                    schoolName = schoolTitle,
                    gender = "female",
                    classId = classId,
                    userType = "teacher"
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
                image = image.toDto()
            )
            CurrentUser().saveCurrentUser(user = currentUser, activity = requireActivity())
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
                classTitle = classTitleList[index]
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
                if (index != schoolCurrentIndex) viewModel.getClasses(schoolsList[index].classesIds)
                schoolCurrentIndex = index
                schoolTitle = schoolsTitleList[index]
                binding().schoolTextView.text = schoolTitle
            }
            .create()
        dialog.show()
    }
}