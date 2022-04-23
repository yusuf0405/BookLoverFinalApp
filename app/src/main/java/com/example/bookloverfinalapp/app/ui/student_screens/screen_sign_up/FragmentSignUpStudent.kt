package com.example.bookloverfinalapp.app.ui.student_screens.screen_sign_up

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.domain.models.classes.Class
import com.example.domain.models.school.School
import com.example.domain.models.student.User
import com.example.domain.models.student.UserImage
import com.example.domain.models.student.UserSignUpRes
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.ui.student_screens.screen_main.ActivityStudentMain
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.app.utils.navigation.CheсkNavigation
import com.example.bookloverfinalapp.databinding.FragmentSignUpStudentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.onEach
import java.util.*

@InternalCoroutinesApi
@AndroidEntryPoint
class FragmentSignUpStudent :
    BaseFragment<FragmentSignUpStudentBinding, FragmentSignUpStudentViewModel>(
        FragmentSignUpStudentBinding::inflate),
    View.OnClickListener, AdapterView.OnItemSelectedListener {

    override val viewModel: FragmentSignUpStudentViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}

    private val classesTitleList = mutableListOf<String>()
    private var classesList = mutableListOf<Class>()

    private val schoolsTitleList = mutableListOf<String>()
    private var schoolsList = mutableListOf<School>()

    private var classesId = ""
    private var classesTitle = ""
    private var schoolTitle = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        observeRecourse()
    }

    private fun observeRecourse() {
        viewModel.schools.onEach { schools ->
            schoolsList = schools.toMutableList()
            schools.forEach { schoolsTitleList.add(it.title) }
            setupSchoolsAdapter()
        }.launchWhenStarted(lifecycleScope = lifecycleScope)

        viewModel.classes.onEach { classes ->
            classesList = classes.toMutableList()
            classesTitleList.clear()
            classes.forEach { classesTitleList.add(it.title) }
            setupClassesAdapter()
        }.launchWhenStarted(lifecycleScope = lifecycleScope)
    }

    private fun setOnClickListeners() {
        binding().apply {
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            signInLink.setOnClickListener(this@FragmentSignUpStudent)
            signUpBtn.setOnClickListener(this@FragmentSignUpStudent)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View) {
        when (view) {
            binding().signUpBtn -> signUp()
            binding().signInLink -> viewModel.goOverLoginFragment()
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
                val studentDto = UserSignUpRes(
                    name = firstNameField.text.toString(),
                    lastname = lastNameField.text.toString(),
                    email = emailField.text.toString(),
                    password = passwordField.text.toString(),
                    number = "+996" + phoneField.text.toString(),
                    className = classesTitle,
                    schoolName = schoolTitle,
                    gender = gender,
                    classId = classesId,
                    userType = "student"
                )
                viewModel.signUp(user = studentDto).observe(viewLifecycleOwner) { user ->
                    signUpSuccess(
                        id = user.id,
                        gender = gender,
                        createdAt = user.createdAt,
                        sessionToken = user.sessionToken,
                        image = user.image
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
                className = classesTitle,
                schoolName = schoolTitle,
                classId = classesId,
                createAt = createdAt,
                userType = "student",
                sessionToken = sessionToken,
                image = image
            )
            CurrentUser().saveCurrentUser(user = currentStudent, activity = requireActivity())
            CheсkNavigation().observeLogin(status = true, activity = requireActivity())
            intentClearTask(activity = ActivityStudentMain())
        }
    }

    private fun setupSchoolsAdapter() {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.item_custom_spinner, schoolsTitleList)
        binding().schoolSpinner.adapter = dataAdapter
        binding().schoolSpinner.onItemSelectedListener = this@FragmentSignUpStudent
    }

    private fun setupClassesAdapter() {
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.item_custom_spinner, classesTitleList)
        binding().classesSpinner.adapter = dataAdapter
        binding().classesSpinner.onItemSelectedListener = this@FragmentSignUpStudent
    }


    override fun onItemSelected(adapter: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
        if (adapter == binding().classesSpinner)
            when (position) {
                0 -> {
                    classesTitle = classesTitleList[0]
                    classesId = classesList[0].objectId
                }
                1 -> {
                    classesTitle = classesTitleList[1]
                    classesId = classesList[1].objectId
                }
                2 -> {
                    classesTitle = classesTitleList[2]
                    classesId = classesList[2].objectId
                }
                3 -> {
                    classesTitle = classesTitleList[3]
                    classesId = classesList[3].objectId
                }
                4 -> {
                    classesTitle = classesTitleList[4]
                    classesId = classesList[4].objectId
                }
                5 -> {
                    classesTitle = classesTitleList[5]
                    classesId = classesList[5].objectId
                }
                6 -> {
                    classesTitle = classesTitleList[6]
                    classesId = classesList[6].objectId
                }
                7 -> {
                    classesTitle = classesTitleList[7]
                    classesId = classesList[7].objectId
                }
                8 -> {
                    classesTitle = classesTitleList[8]
                    classesId = classesList[8].objectId
                }
                9 -> {
                    classesTitle = classesTitleList[9]
                    classesId = classesList[9].objectId
                }
                10 -> {
                    classesTitle = classesTitleList[10]
                    classesId = classesList[10].objectId
                }
                11 -> {
                    classesTitle = classesTitleList[11]
                    classesId = classesList[11].objectId
                }
                12 -> {
                    classesTitle = classesTitleList[12]
                    classesId = classesList[12].objectId
                }
            } else
            when (position) {
                0 -> {
                    schoolTitle = schoolsTitleList[0]
                    viewModel.getClasses(schoolsList[0].classesIds)
                }
                1 -> {
                    schoolTitle = schoolsTitleList[1]
                    viewModel.getClasses(schoolsList[1].classesIds).toString()
                }
                2 -> {
                    schoolTitle = schoolsTitleList[2]
                    viewModel.getClasses(schoolsList[2].classesIds).toString()
                }
                3 -> {
                    schoolTitle = schoolsTitleList[3]
                    viewModel.getClasses(schoolsList[3].classesIds).toString()
                }
                4 -> {
                    schoolTitle = schoolsTitleList[4]
                    viewModel.getClasses(schoolsList[4].classesIds).toString()
                }
                5 -> {
                    schoolTitle = schoolsTitleList[5]
                    viewModel.getClasses(schoolsList[5].classesIds).toString()
                }
                6 -> {
                    schoolTitle = schoolsTitleList[6]
                    viewModel.getClasses(schoolsList[6].classesIds).toString()
                }
                7 -> {
                    schoolTitle = schoolsTitleList[7]
                    viewModel.getClasses(schoolsList[7].classesIds).toString()
                }
                8 -> {
                    schoolTitle = schoolsTitleList[8]
                    viewModel.getClasses(schoolsList[8].classesIds).toString()
                }
                9 -> {
                    schoolTitle = schoolsTitleList[9]
                    viewModel.getClasses(schoolsList[9].classesIds).toString()
                }
                10 -> {
                    schoolTitle = schoolsTitleList[10]
                    viewModel.getClasses(schoolsList[10].classesIds).toString()
                }
                11 -> {
                    schoolTitle = schoolsTitleList[11]
                    viewModel.getClasses(schoolsList[11].classesIds).toString()
                }
                12 -> {
                    schoolTitle = schoolsTitleList[12]
                    viewModel.getClasses(schoolsList[12].classesIds).toString()
                }
            }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

}