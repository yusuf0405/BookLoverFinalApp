package com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseFragment
import com.example.bookloverfinalapp.app.ui.general_screens.screen_create_question.choose_time.FragmentChooseTimeDialog
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.FragmentCreateNewQuestionBinding
import com.joseph.ui_core.extensions.toDp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCreateNewQuestion :
    BaseFragment<FragmentCreateNewQuestionBinding, FragmentCreateNewQuestionViewModel>(
        FragmentCreateNewQuestionBinding::inflate
    ) {

    override val viewModel: FragmentCreateNewQuestionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
        addCoverImageBlock.setOnDownEffectClickListener { pickImageFileInStorage() }
        questionPoster.setOnDownEffectClickListener { pickImageFileInStorage() }
        questionTimeLimitContainer.setOnDownEffectClickListener { showFragmentChooseTimeDialog() }
    }

    private fun pickImageFileInStorage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultPickQuestionPoster.launch(intent)
    }

    private val resultPickQuestionPoster =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val data: Intent? = result.data
            data?.data?.let(::handlePickPosterResult)
        }

    private fun handlePickPosterResult(uri: Uri) = with(binding()) {
        questionPoster.show()
        requireContext().showRoundedImage(
            roundedSize = 12.toDp,
            imageUri = uri,
            questionPoster
        )
        addCoverImageBlock.hide()
    }

    private fun showFragmentChooseTimeDialog() =
        FragmentChooseTimeDialog().showOnlyOne(parentFragmentManager)
}