package com.example.bookloverfinalapp.app.ui.general_screens.screen_edit_profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.cons.RESULT_LOAD_IMAGE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.FragmentEditProfileBinding
import com.example.domain.models.UserUpdateDomain
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseFile
import com.parse.SaveCallback
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
@AndroidEntryPoint
class FragmentEditProfile :
    BaseFragment<FragmentEditProfileBinding, FragmentEditProfileViewModel>(
        FragmentEditProfileBinding::inflate), View.OnClickListener {

    override val viewModel: FragmentEditProfileViewModel by viewModels()

    private var parseFile: ParseFile? = null
    private var image: UserImage? = null

    private lateinit var user: User
    private lateinit var gender: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding().apply {
            downEffect(saveEditButton).setOnClickListener(this@FragmentEditProfile)
            downEffect(editStudentImage).setOnClickListener(this@FragmentEditProfile)
            female.setOnClickListener(this@FragmentEditProfile)
            male.setOnClickListener(this@FragmentEditProfile)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().saveEditButton -> checkChanges()
            binding().editStudentImage -> getImage()
            binding().male -> gender = "male"
            binding().female -> gender = "female"
        }
    }


    private fun setupUi() {
        user = checkNotNull(SharedPreferences().getCurrentUser(activity = requireActivity()))
        binding().apply {
            setCardViewColor(materialCardView)
            gender = user.gender
            editStudentNumber.setText(user.number)
            editStudentName.setText(user.name)
            editStudentLastName.setText(user.lastname)
            editStudentEmail.setText(user.email)
            if (user.gender == "female") female.isChecked = true
            else male.isChecked = true
            requireContext().glide(user.image?.url, binding().profileImg)
            toolbar.title = getString(R.string.my_profile)
            setToolbarColor(toolbar = toolbar)

        }
    }

    private fun checkChanges() {
        binding().apply {
            if (editStudentName.text.toString() == user.name &&
                editStudentLastName.text.toString() == user.lastname &&
                editStudentEmail.text.toString() == user.email &&
                editStudentNumber.text.toString() == user.number &&
                gender == user.gender && parseFile == null
            ) return@apply
            else checkImage()
        }
    }

    private fun checkImage() {
        binding().apply {
            if (!editStudentName.validateName()) showToast(message = getString(R.string.name_input_format_error))
            else if (!editStudentLastName.validateLastName()) showToast(message = getString(R.string.last_name_input_format_error))
            else if (!editStudentNumber.validatePhone()) showToast(message = getString(R.string.phone_input_format_error))
            else if (!editStudentEmail.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
            else {
                if (parseFile == null) {
                    image = user.image
                    updateStudent()
                } else {
                    loadingDialog.show()
                    saveImage()
                }
            }
        }
    }

    private fun saveImage() {
        parseFile!!.saveInBackground(SaveCallback {
            if (it == null) {
                image = parseFile!!.toImage()
                updateStudent()
            } else {
                loadingDialog.dismiss()
                showToast(it.message!!)
            }
        })
    }

    private fun updateStudent() {
        binding().apply {
            val student = UserUpdateDomain(
                gender = gender,
                name = editStudentName.text.toString(),
                lastname = editStudentLastName.text.toString(),
                email = editStudentEmail.text.toString(),
                number = editStudentNumber.text.toString(),
                image = image!!.toDto()
            )
            viewModel.updateStudent(id = this@FragmentEditProfile.user.id,
                user = student, sessionToken = currentUser.sessionToken)
                .observe(viewLifecycleOwner) { successUpdated() }
        }
    }

    private fun successUpdated() {
        binding().apply {
            val newStudent = User(
                gender = gender,
                name = editStudentName.text.toString(),
                lastname = editStudentLastName.text.toString(),
                email = editStudentEmail.text.toString(),
                password = user.password.toString(),
                number = editStudentNumber.text.toString(),
                image = image!!,
                classId = user.classId,
                className = user.className,
                schoolName = user.schoolName,
                id = user.id,
                createAt = user.createAt,
                userType = user.userType,
                sessionToken = user.sessionToken,
                schoolId = user.schoolId
            )
            SharedPreferences().saveCurrentUser(user = newStudent, requireActivity())
            user = checkNotNull(SharedPreferences().getCurrentUser(activity = requireActivity()))
            showToast(message = getString(R.string.profile_has_been_successfully_updated))
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val uri = data.data!!
            parseFile = ParseFile("image.png", uriToImage(uri))
            requireContext().glide(uri, binding().profileImg)
        }
    }


    override fun onStart() {
        super.onStart()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }
}