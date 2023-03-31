package com.joseph.stories.presentation.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.joseph.common_api.base.BaseBindingFragment
import com.joseph.stories.R
import com.joseph.stories.databinding.FragmentCreateNewStoriesBinding
import com.joseph.stories.presentation.models.MediaType
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import com.joseph.ui_core.dialog.UploadFileDialog
import com.joseph.ui_core.extensions.launchWhenViewStarted
import com.joseph.utils_core.extensions.setOnDownEffectClickListener
import com.joseph.utils_core.extensions.showOnlyOne
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCreateNewStories :
    BaseBindingFragment<FragmentCreateNewStoriesBinding>(FragmentCreateNewStoriesBinding::inflate) {

    private val viewModel: FragmentCreateNewStoriesViewModel by viewModels()

    private var byteArray: ByteArray? = null
    private var mediaType: MediaType? = null

    private val uploadFileDialog: UploadFileDialog by lazy(LazyThreadSafetyMode.NONE) {
        UploadFileDialog.getInstance()
    }

//    private val progressDialog: ProgressDialog by lazy(LazyThreadSafetyMode.NONE) {
//        ProgressDialog.getInstance()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeResource()
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        confirmButton.setOnDownEffectClickListener { handleConfirmButtonClick() }
    }

    private fun observeResource() = with(viewModel) {
        launchWhenViewStarted {
            imageUploadDialogIsShowFlow.observe(::handleImageUploadDialogIsShow)
            imageUploadDialogPercentFlow.observe(::setUploadFileDialogTitle)
            isShowProgressDialogFlow.observe(::handleProgressDialogState)
            startAddNewStories.observe { showSuccessUploadDialog() }
            storiesAddSuccessfully.observe { handleStoriesAddSuccessfully() }
        }
    }

    private fun handleStoriesAddSuccessfully() {
        showSuccessSnackbar(getString(R.string.stories_added_successfully))
        dismissModalPage()
    }

    private fun setUploadFileDialogTitle(progress: Int) {
        uploadFileDialog.setTitle(
            progress = progress,
            description = getString(R.string.uploading_files)
        )
    }

    private fun showSuccessUploadDialog() {
        uploadFileDialog.showSuccessBlock {
            startAddNewStories()
            uploadFileDialog.dismiss()
        }
    }

    private fun handleImageUploadDialogIsShow(isShow: Boolean) {
        if (isShow) uploadFileDialog.showOnlyOne(parentFragmentManager)
        else uploadFileDialog.dismiss()
    }

    private fun handleProgressDialogState(isShow: Boolean) {
        if (isShow) showProgressDialog()
        else dismissProgressDialog()
    }

    private fun handleConfirmButtonClick() {
        when {
            title().isEmpty() -> showErrorSnackbar(getString(R.string.fill_in_all_fields))
            description().isEmpty() -> showErrorSnackbar(getString(R.string.fill_in_all_fields))
            else -> viewModel.startSaveFile(byteArray)
        }
    }

    private fun startAddNewStories() {
        viewModel.addNewStories(
            title = title(),
            description = description(),
            isVideoFile = mediaType == MediaType.VIDEO,
        )
    }

    private fun title() = binding().titleInput.text.toString()

    private fun description() = binding().descriptionInput.text.toString()

    private fun showProgressDialog() {
//        progressDialog.showOnlyOne(parentFragmentManager)
    }

    private fun dismissProgressDialog() {
//        progressDialog.dismiss()
    }

    companion object {

        fun newInstance(
            byteArray: ByteArray,
            mediaType: MediaType
        ) = FragmentCreateNewStories().run {
            this.byteArray = byteArray
            this.mediaType = mediaType
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}