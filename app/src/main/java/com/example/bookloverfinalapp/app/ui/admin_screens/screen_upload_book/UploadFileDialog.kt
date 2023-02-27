package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.*
import com.example.bookloverfinalapp.databinding.DialogUploadBookBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFileDialog : DialogFragment() {

    private var _binding: DialogUploadBookBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogUploadBookBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tuneLyricsDialog()
        tuneCenterDialog()
        initAndApplyArguments()
    }

    fun setTitle(progress: Int, description: String = getString(R.string.loading_the_book)) {
        val progressText = "$progress%"
        binding.procent.text = progressText
        binding.description.text = description
    }

    fun showSuccessBlock(action: () -> Unit) = with(binding) {
        successBlock.isVisible = true
        uploadBlock.isVisible = false
        continueBtn.setOnDownEffectClickListener { action() }
    }

    private fun initAndApplyArguments() = runWithArgumentsOrSkip { arguments ->
        val title = arguments.getString(TITLE_KEY)
        val isCancelableFromArgument = arguments.getBoolean(IS_CANCELABLE_KEY)
        isCancelable = isCancelableFromArgument
        if (title.isNullOrEmpty()) return@runWithArgumentsOrSkip

    }

    companion object {
        private const val TITLE_KEY = "TITLE_KEY"
        private const val IS_CANCELABLE_KEY = "IS_CANCELABLE_KEY"

        fun getInstance(title: String = String(), isCancelable: Boolean = false): UploadFileDialog {
            val dialog = UploadFileDialog()
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, title)
            bundle.putBoolean(IS_CANCELABLE_KEY, isCancelable)
            dialog.arguments = bundle
            return dialog
        }
    }
}