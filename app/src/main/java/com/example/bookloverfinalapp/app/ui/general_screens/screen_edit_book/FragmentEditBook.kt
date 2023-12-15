package com.example.bookloverfinalapp.app.ui.general_screens.screen_edit_book

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.joseph.ui.core.R
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.joseph.ui.core.dialog.UploadFileDialog
import com.example.bookloverfinalapp.app.ui.general_screens.ProgressDialog
import com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.FragmentProfile
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentEditBookBinding
import com.example.domain.models.UpdateBookDomain
import com.joseph.ui.core.extensions.launchWhenViewStarted
import com.joseph.core.extensions.showImage
import com.joseph.core.extensions.showOnlyOne
import com.joseph.core.extensions.showRoundedImage
import com.parse.ParseFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentEditBook :
    BaseFragment<FragmentEditBookBinding, FragmentEditBookViewModel>(FragmentEditBookBinding::inflate) {

    override val viewModel: FragmentEditBookViewModel by viewModels()

    private val bookId: String by lazy(LazyThreadSafetyMode.NONE) {
        FragmentEditBookArgs.fromBundle(requireArguments()).bookId
    }

    private val progressDialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
        ProgressDialog.getInstance()
    }

    private val uploadFileDialog: UploadFileDialog by lazy(LazyThreadSafetyMode.NONE) {
        UploadFileDialog.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
        observeData()
    }

    private fun setupViews() = with(binding()) {
        toolbarBlock.title.setText(R.string.edit_book)
        requireContext().showRoundedImage(
            roundedSize = 12,
            R.drawable.genre_background_first,
            poster
        )
        toolbarBlock.sortOptions.hide()
    }

    private fun setOnClickListeners() = with(binding()) {
        saveButton.setOnDownEffectClickListener {
            viewModel.updateBook(bookId = bookId, updateBook = createUpdateBook())
        }
        poster.setOnDownEffectClickListener { pickImageFileInStorage() }
    }

    private fun observeData() = with(viewModel) {
        launchWhenViewStarted {
            isShowLoadingDialog.observe(::handleShowProgressDialog)
            imageUploadDialogPercentFlow.observe(::setUploadFileDialogTitle)
            imageUploadDialogIsShowFlow.observe(::handleImageUploadDialogIsShow)
            startUpdateUserFlow.observe { }
        }
    }

    private fun pickImageFileInStorage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultPickBookPoster.launch(intent)
    }

    private val resultPickBookPoster = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
        val data: Intent? = result.data
        data?.data?.let { uri -> handlePickPosterResult(convertImageUriToByteArray(uri)) }
    }

    private fun setUploadFileDialogTitle(progress: Int) {
        uploadFileDialog.setTitle(
            progress = progress,
            description = getString(R.string.uploading_a_new_avatar)
        )
    }

    private fun handleImageUploadDialogIsShow(isShow: Boolean) {
        if (isShow) uploadFileDialog.showOnlyOne(parentFragmentManager)
        else uploadFileDialog.dismiss()
    }


    private fun handlePickPosterResult(byteArray: ByteArray) {
        viewModel
            .updateCurrentUserImageFile(ParseFile(FragmentProfile.DEFAULT_IMAGE_TITLE, byteArray))
        requireContext().showImage(byteArray, binding().poster)
    }

    private fun handleShowProgressDialog(isShow: Boolean) {
        if (isShow) progressDialog.showOnlyOne(parentFragmentManager)
        else progressDialog.dismiss()
    }

    private fun createUpdateBook() = UpdateBookDomain(
        title = title(),
        author = author(),
        publicYear = publicYear(),
        poster = throw IllegalStateException()
    )

    private fun title() = binding().enterTitle.text.toString()

    private fun author() = binding().enterAuthor.text.toString()

    private fun publicYear() = binding().enterDate.text.toString()
}