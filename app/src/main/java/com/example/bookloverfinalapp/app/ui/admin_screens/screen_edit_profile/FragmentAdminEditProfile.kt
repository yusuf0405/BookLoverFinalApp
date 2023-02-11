package com.example.bookloverfinalapp.app.ui.admin_screens.screen_edit_profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserGender
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.cons.RESULT_LOAD_IMAGE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.SharedPreferences
import com.example.bookloverfinalapp.databinding.FragmentAdminEditProfileBinding
import com.example.domain.models.UserUpdateDomain
import com.parse.ParseFile
import com.parse.SaveCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentAdminEditProfile :
    BaseFragment<FragmentAdminEditProfileBinding, FragmentAdminEditProfileViewModel>(
        FragmentAdminEditProfileBinding::inflate
    ), View.OnClickListener {

    override val viewModel: FragmentAdminEditProfileViewModel by viewModels()

    private var parseFile: ParseFile? = null
    private var image: UserImage? = null

    private lateinit var admin: User
    private lateinit var gender: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        setupUi()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
//        binding.apply {
//            saveEditButton.setOnDownEffectClickListener(this@FragmentAdminEditProfile)
//            editStudentImage.setOnDownEffectClickListener(this@FragmentAdminEditProfile)
//            female.setOnDownEffectClickListener(this@FragmentAdminEditProfile)
//            male.setOnDownEffectClickListener(this@FragmentAdminEditProfile)
//        }
    }

    override fun onClick(view: View?) {
        when (view) {
//            binding.saveEditButton -> checkChanges()
//            binding.editStudentImage -> getImage()
//            binding.male -> gender = "male"
//            binding.female -> gender = "female"
        }
    }


    private fun setupUi() {
        admin = checkNotNull(SharedPreferences().getCurrentUser(activity = requireActivity()))
//        binding.apply {
//            setCardViewColor(materialCardView)
//            gender = admin.gender.name
//            editStudentNumber.setText(admin.number)
//            editStudentName.setText(admin.name)
//            editStudentLastName.setText(admin.lastname)
//            editStudentEmail.setText(admin.email)
//            requireContext().showImage(admin.image?.url, binding.profileImg)
//        }
    }


    private fun checkImage() {
//        binding.apply {
//            if (!editStudentName.validateName()) showToast(message = getString(R.string.name_input_format_error))
//            else if (!editStudentLastName.validateLastName()) showToast(message = getString(R.string.last_name_input_format_error))
//            else if (!editStudentNumber.validatePhone()) showToast(message = getString(R.string.phone_input_format_error))
//            else if (!editStudentEmail.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
//            else {
//                if (parseFile == null) {
//                    image = admin.image
//                    updateStudent()
//                } else {
//                    loadingDialog.show()
//                    saveImage()
//                }
//            }
//        }
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
//        binding.apply {
//            val student = UserUpdateDomain(
//                gender = gender,
//                name = editStudentName.text.toString(),
//                lastname = editStudentLastName.text.toString(),
//                email = editStudentEmail.text.toString(),
//                number = editStudentNumber.text.toString(),
//                image = image!!.toDto()
//            )
//            viewModel.updateAdmin(
//                id = this@FragmentAdminEditProfile.admin.id,
//                user = student, sessionToken = currentUser.sessionToken
//            )
//                .observe(viewLifecycleOwner) { successUpdated() }
//        }
    }

    private fun successUpdated() {
//        binding.apply {
//            val newStudent = User(
//                gender = UserGender.female,
//                name = editStudentName.text.toString(),
//                lastname = editStudentLastName.text.toString(),
//                email = editStudentEmail.text.toString(),
//                password = admin.password.toString(),
//                number = editStudentNumber.text.toString(),
//                image = image!!,
//                classId = admin.classId,
//                className = admin.className,
//                schoolName = admin.schoolName,
//                id = admin.id,
//                createAt = admin.createAt,
//                userType = admin.userType,
//                sessionToken = admin.sessionToken,
//                schoolId = admin.schoolId
//            )
//            SharedPreferences().saveCurrentUser(user = newStudent, requireActivity())
//            admin = checkNotNull(SharedPreferences().getCurrentUser(activity = requireActivity()))
//            showToast(message = getString(R.string.profile_has_been_successfully_updated))
//        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val uri = data.data!!
            parseFile = ParseFile("image.png", uriToImage(uri))
        }
    }
}