package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.models.User
import com.example.bookloverfinalapp.app.models.UserGender
import com.joseph.core.motion.MotionListener
import com.joseph.core.motion.MotionState
import com.joseph.ui.core.dialog.UploadFileDialog
import com.example.bookloverfinalapp.app.ui.general_screens.ProgressDialog
import com.example.bookloverfinalapp.app.ui.general_screens.activity_main.OnBackPressedListener
import com.example.bookloverfinalapp.app.ui.general_screens.choose_poster.FragmentChooseQuestionPoster
import com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting.FragmentSetting
import com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.choice_image_dialog.FragmentChoiceImage
import com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.login_out.FragmentLoginOutDialog
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentProfileBinding
import com.example.domain.models.UserUpdateDomain
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.convertDrawableToByteArray
import com.joseph.core.extensions.showImage
import com.joseph.core.extensions.showOnlyOne
import com.parse.ParseFile
import dagger.hilt.android.AndroidEntryPoint
import com.example.bookloverfinalapp.R as MainRes

@AndroidEntryPoint
class FragmentProfile :
    BaseFragment<FragmentProfileBinding, FragmentProfileViewModel>(FragmentProfileBinding::inflate),
    OnBackPressedListener {

    override val viewModel: FragmentProfileViewModel by viewModels()

    private var gender = UserGender.male

    private val progressDialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
        ProgressDialog.getInstance()
    }

    private val uploadFileDialog: UploadFileDialog by lazy(LazyThreadSafetyMode.NONE) {
        UploadFileDialog.getInstance()
    }

    private val choiceImageDialog = FragmentChoiceImage.newInstance(
        pickInGalleryListener = { pickImageFileInStorage() },
        pickInCameraListener = { pickImageFileInCamera() },
        choiceImageListener = { resourceDrawable -> choiceDefaultImage(resourceDrawable) }
    )

    private val motionListener = MotionListener(::setToolbarState)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigationView()
        setOnClickListeners()
        observeData()
        setupViews()
    }

    private fun setupViews() = with(binding()) {
        root.addTransitionListener(motionListener)
    }

    private fun setOnClickListeners() = with(binding()) {
        toolbar.setOnDownEffectClickListener { startMotionSceneToStart() }
        with(fragmentEditProfile) {
            saveEditButton.setOnDownEffectClickListener { checkChanges() }
            changeImage.setOnDownEffectClickListener { showFragmentChooseQuestionPoster() }
            female.setOnDownEffectClickListener { gender = UserGender.female }
            male.setOnDownEffectClickListener { gender = UserGender.male }
        }
        with(fragmentProfile) {
            editProfileBtn.setOnDownEffectClickListener { startMotionSceneToEnd() }
            loginOutBlock.setOnDownEffectClickListener {
                showFragmentLoginOutDialog()
            }
            navigateToSetting.setOnDownEffectClickListener { showSettingModalPage() }
        }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            currentUserFlow.observe(::handleCurrentUser)
            saveNewUserStatusFlow.observe(::handleUserUpdated)
            imageUploadDialogPercentFlow.observe(::setUploadFileDialogTitle)
            imageUploadDialogIsShowFlow.observe(::handleImageUploadDialogIsShow)
            startUpdateUserFlow.observe { startUpdateUser() }
            userSuccessUpdatedFlow.observe { handleSuccessUpdated() }
        }
    }

    private fun handleCurrentUser(user: User) = with(binding()) {
        with(fragmentProfile) {
            val fullName = "${user.name} ${user.lastname}"
            profileNameText.text = fullName
            profileSchoolText.text = user.schoolName
            profileClassText.text = user.className
            profileTelephoneNumber.text = user.number
            requireActivity().showImage(user.image?.url, binding().profileImg)
        }
        with(fragmentEditProfile) {
            gender = user.gender
            editStudentNumber.setText(user.number)
            editStudentName.setText(user.name)
            editStudentLastName.setText(user.lastname)
            editStudentEmail.setText(user.email)
            if (gender == UserGender.female) female.isChecked = true
            else male.isChecked = true
            requireContext().showImage(user.image?.url, profileAvatarBlock.profileImg)
        }
    }

    private fun createActivityResultContracts() = ActivityResultContracts.StartActivityForResult()

    private fun pickImageFileInStorage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultPickUserAvatar.launch(intent)
    }

    private val resultPickUserAvatar = registerForActivityResult(
        createActivityResultContracts()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val data: Intent? = result.data
        data?.data?.let { uri -> handlePickPosterResult(convertImageUriToByteArray(uri)) }
    }

    private fun pickImageFileInCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        resultCamera.launch(intent)
    }

    private val resultCamera = registerForActivityResult(
        createActivityResultContracts()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val photo = result.data?.extras?.get(DATA) as? Bitmap ?: return@registerForActivityResult
        handlePickPosterResult(convertBitmapToByteArray(photo))
    }

    private fun choiceDefaultImage(resourceId: Int) {
        val resourceDrawable = ContextCompat.getDrawable(requireContext(), resourceId) ?: return
        handlePickPosterResult(requireContext().convertDrawableToByteArray(resourceDrawable))
    }

    private fun setToolbarState(state: MotionState) {
        when (state) {
            MotionState.COLLAPSED -> viewModel.updateMotionPosition(COLLAPSED)
            MotionState.EXPANDED -> viewModel.updateMotionPosition(EXPANDED)
            else -> Unit
        }
    }

    private fun checkChanges() = with(binding()) {
        val user = viewModel.getCurrentUser()
        if (name() == user.name && lastName() == user.lastname &&
            email() == user.email && number() == user.number && gender == user.gender &&
            viewModel.currentUserImageFile.value == null
        ) {
            showInfoSnackBar(getString(R.string.enter_the_change))
            return
        }
        checkParametersFormat()
    }


    private fun checkParametersFormat() = with(binding().fragmentEditProfile) {
        when {
            !editStudentName.validateName() -> showErrorSnackbar(message = getString(R.string.name_input_format_error))
            !editStudentLastName.validateLastName() -> showErrorSnackbar(message = getString(R.string.last_name_input_format_error))
            !editStudentNumber.validatePhone() -> showErrorSnackbar(message = getString(R.string.phone_input_format_error))
            !editStudentEmail.validateEmail() -> showErrorSnackbar(message = getString(R.string.email_input_format_error))
            else -> viewModel.checkParseFileAndStartUserUpdate()
        }
    }

    private fun startUpdateUser() = with(binding()) {
        showProgressDialog()
        val newUser = UserUpdateDomain(
            gender = gender.name,
            name = name(),
            lastname = lastName(),
            email = email(),
            number = number(),
            image = viewModel.currentUserImage.value!!.toDto()
        )
        viewModel.updateUser(newUser)
    }

    private fun setUploadFileDialogTitle(progress: Int) {
        uploadFileDialog.setTitle(
            progress = progress,
            description = getString(R.string.uploading_a_new_avatar)
        )
    }

    private fun handleImageUploadDialogIsShow(isShow: Boolean) {
        if (isShow) uploadFileDialog.showOnlyOne(parentFragmentManager)
        else {
            uploadFileDialog.showSuccessBlock {
                startUpdateUser()
                uploadFileDialog.dismiss()
            }
        }
    }

    private fun handleSuccessUpdated() = with(binding()) {
        val user = viewModel.getCurrentUser()
        val newUser = User(
            gender = gender,
            name = name(),
            lastname = lastName(),
            email = email(),
            password = user.password.toString(),
            number = number(),
            image = viewModel.currentUserImage.value,
            classId = user.classId,
            className = user.className,
            schoolName = user.schoolName,
            id = user.id,
            createAt = user.createAt,
            userType = user.userType,
            sessionToken = user.sessionToken,
            schoolId = user.schoolId
        )
        viewModel.saveNewUserFromCache(newUser)
    }

    private fun handlePickPosterResult(byteArray: ByteArray) {
        viewModel.updateCurrentUserImageFile(ParseFile(DEFAULT_IMAGE_TITLE, byteArray))
        requireContext().showImage(byteArray, binding().profileImg)
    }

    private fun handleUserUpdated(isSuccess: Boolean) {
        dismissProgressDialog()
        if (isSuccess) showSuccessSnackBar(message = getString(R.string.profile_has_been_successfully_updated))
    }

    private fun name() = binding().fragmentEditProfile.editStudentName.text.toString()

    private fun lastName() = binding().fragmentEditProfile.editStudentLastName.text.toString()

    private fun email() = binding().fragmentEditProfile.editStudentEmail.text.toString()

    private fun number() = binding().fragmentEditProfile.editStudentNumber.text.toString()

    private fun showFragmentLoginOutDialog() = FragmentLoginOutDialog.newInstance()
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun startMotionSceneToStart() = binding().root.transitionToState(MainRes.id.start)

    private fun startMotionSceneToEnd() = binding().root.transitionToState(MainRes.id.end)

    private fun showFragmentChoiceImage() = choiceImageDialog
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showFragmentChooseQuestionPoster() = FragmentChooseQuestionPoster.newInstance(
        onNetworkPosterSelectedListener = {},
        onLocalPosterSelectedListener = {}
    ).show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showSettingModalPage() = FragmentSetting.newInstance(getString(R.string.setting))
        .show(requireActivity().supportFragmentManager, ModalPage.TAG)

    private fun showProgressDialog() = progressDialog.showOnlyOne(parentFragmentManager)

    private fun dismissProgressDialog() = progressDialog.dismiss()

    override fun onStart() {
        super.onStart()
        showBottomNavigationView()
        binding().root.progress = viewModel.motionPosition.value
    }

    override fun onDestroyView() {
        binding().root.removeTransitionListener(motionListener)
        super.onDestroyView()
    }

    override fun onBackPressed(): Boolean = if (viewModel.motionPosition.value == COLLAPSED) {
        startMotionSceneToStart()
        true
    } else false

    companion object {
        const val DEFAULT_IMAGE_TITLE = "image.png"
        const val DATA = "data"
    }
}