package com.example.bookloverfinalapp.app.ui.screen_edit_profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserImage
import com.example.bookloverfinalapp.app.utils.cons.RESULT_LOAD_IMAGE
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.app.utils.pref.CurrentUser
import com.example.bookloverfinalapp.databinding.FragmentEditProfileBinding
import com.example.domain.models.student.UserUpdateDomain
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.parse.ParseFile
import com.parse.SaveCallback
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
@AndroidEntryPoint
class FragmentEditProfile :
    BaseFragment<FragmentEditProfileBinding, FragmentEditProfileViewModel>(
        FragmentEditProfileBinding::inflate), View.OnClickListener {

    override val viewModel: FragmentEditProfileViewModel by viewModels()

    override fun onReady(savedInstanceState: Bundle?) {}
    private var parseFile: ParseFile? = null
    private var image: UserImage? = null

    private lateinit var student: User
    private lateinit var gender: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding().apply {
            saveEditButton.setOnClickListener(this@FragmentEditProfile)
            editStudentImage.setOnClickListener(this@FragmentEditProfile)
            confirmPasswordBtn.setOnClickListener(this@FragmentEditProfile)
            female.setOnClickListener(this@FragmentEditProfile)
            male.setOnClickListener(this@FragmentEditProfile)
            toolbar.setNavigationOnClickListener { viewModel.goBack() }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding().saveEditButton -> checkChanges()
            binding().editStudentImage -> getImage()
            binding().confirmPasswordBtn -> chekConfirmPassword()
            binding().male -> gender = "male"
            binding().female -> gender = "female"
        }
    }

    private fun chekConfirmPassword() {
        binding().apply {
            if (editStudentPasswordConfirm.text.toString() == student.password) {
                editStudentNumber.isEnabled = true
                editStudentPassword.isEnabled = true
                editStudentEmail.isEnabled = true
            } else {
                editStudentNumber.isEnabled = false
                editStudentPassword.isEnabled = false
                editStudentEmail.isEnabled = false
                showToast(message = getString(R.string.invalid_password_error))
            }
        }
    }

    private fun setupUi() {
        student = CurrentUser().getCurrentUser(activity = requireActivity())
        binding().apply {
            gender = student.gender
            editStudentPassword.setText(student.password)
            editStudentNumber.setText(student.number)
            editStudentName.setText(student.name)
            editStudentLastName.setText(student.lastname)
            editStudentEmail.setText(student.email)
            if (student.gender == "female") female.isChecked = true
            else male.isChecked = true
            Glide.with(requireActivity())
                .load(student.image?.url)
                .placeholder(R.drawable.placeholder_avatar)
                .into(binding().profileImg)
            toolbar.apply {
                title = getString(R.string.my_profile)
                setTitleTextColor(Color.WHITE)
                setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            }
        }
    }

    private fun checkChanges() {
        binding().apply {
            if (editStudentName.text.toString() == student.name &&
                editStudentLastName.text.toString() == student.lastname &&
                editStudentEmail.text.toString() == student.email &&
                editStudentPassword.text.toString() == student.password &&
                editStudentNumber.text.toString() == student.number &&
                gender == student.gender && parseFile == null
            ) return@apply
            else checkImage()
        }
    }

    private fun checkImage() {
        binding().apply {
            if (!editStudentName.validateName()) showToast(message = getString(R.string.name_input_format_error))
            else if (!editStudentLastName.validateLastName()) showToast(message = getString(R.string.last_name_input_format_error))
            else if (!editStudentNumber.validateEditPhone()) showToast(message = getString(R.string.phone_input_format_error))
            else if (!editStudentPassword.validatePassword()) showToast(message = getString(R.string.password_input_format_error))
            else if (!editStudentEmail.validateEmail()) showToast(message = getString(R.string.email_input_format_error))
            else {
                if (parseFile == null) {
                    image = student.image
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
            viewModel.updateStudent(id = this@FragmentEditProfile.student.id,
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
                password = editStudentPassword.text.toString(),
                number = editStudentNumber.text.toString(),
                image = image!!,
                classId = student.classId,
                className = student.className,
                schoolName = student.schoolName,
                id = student.id,
                createAt = student.createAt,
                userType = student.userType,
                sessionToken = student.sessionToken
            )
            CurrentUser().saveCurrentUser(user = newStudent, requireActivity())
            student = CurrentUser().getCurrentUser(activity = requireActivity())
            showToast(message = getString(R.string.profile_has_been_successfully_updated))
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val bitmap =
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, data.data)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray: ByteArray = stream.toByteArray()
            parseFile = ParseFile("image.png", byteArray)
            Glide.with(requireActivity())
                .load(data.dataString)
                .into(binding().profileImg)
        }
    }

    private fun getImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, RESULT_LOAD_IMAGE)
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).hideView()
    }
}