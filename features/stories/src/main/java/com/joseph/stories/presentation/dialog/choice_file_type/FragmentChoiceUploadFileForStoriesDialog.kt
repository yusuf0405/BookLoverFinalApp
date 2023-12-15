package com.joseph.stories.presentation.dialog.choice_file_type

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.joseph.common.base.BaseBindingFragment
import com.joseph.ui.core.R
import com.joseph.stories.databinding.ChoiceUploadFileForStoriesDialogBinding
import com.joseph.stories.presentation.dialog.FragmentCreateNewStories
import com.joseph.stories.presentation.models.MediaType
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import com.joseph.core.extensions.setOnDownEffectClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentChoiceUploadFileForStoriesDialog :
    BaseBindingFragment<ChoiceUploadFileForStoriesDialogBinding>(
        ChoiceUploadFileForStoriesDialogBinding::inflate
    ) {

    private var mediaType = MediaType.IMAGE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        getString(R.string.select_file_type)
        cancelBtn.setOnDownEffectClickListener { dismissModalPage() }
        pickVideoCardView.setOnDownEffectClickListener { pickVideoFileInStorage() }
        takeImageCardView.setOnDownEffectClickListener { pickImageFileInStorage() }
    }

    private fun createActivityResultContracts() = ActivityResultContracts.StartActivityForResult()

    private fun pickImageFileInStorage() {
        mediaType = MediaType.IMAGE
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultPickUserAvatar.launch(intent)
    }

    private val resultPickUserAvatar = registerForActivityResult(
        createActivityResultContracts()
    ) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult
        val uri: Uri = result.data?.data ?: return@registerForActivityResult
//        handlePickFileResult(convertImageUriToByteArray(uri))
    }

    private fun pickVideoFileInStorage() {
        mediaType = MediaType.VIDEO
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        resultVideo.launch(intent)
    }

    private val resultVideo = registerForActivityResult(
        createActivityResultContracts()
    ) { result ->
        if (result.resultCode != RESULT_OK) return@registerForActivityResult
        val uri: Uri = result.data?.data ?: return@registerForActivityResult
//        handlePickFileResult(convertVideoUriToByteArray(uri))
    }


    private fun handlePickFileResult(byteArray: ByteArray) {
        FragmentCreateNewStories.newInstance(
            byteArray = byteArray,
            mediaType = mediaType
        ).show(requireActivity().supportFragmentManager, ModalPage.TAG)
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentChoiceUploadFileForStoriesDialog().run {
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }
}