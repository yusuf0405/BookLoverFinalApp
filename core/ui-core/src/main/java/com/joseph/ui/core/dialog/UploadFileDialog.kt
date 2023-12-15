package com.joseph.ui.core.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.joseph.ui.core.R
import com.joseph.ui.core.databinding.DialogUploadBookBinding
import com.joseph.core.extensions.setOnDownEffectClickListener

class UploadFileDialog : DialogFragment() {

    private var _binding: DialogUploadBookBinding? = null
    private val binding get() = _binding ?: throw java.lang.IllegalStateException()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogUploadBookBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        tuneLyricsDialog()
//        tuneCenterDialog()
        initAndApplyArguments()
    }

    fun setTitle(progress: Int, description: String = getString(R.string.loading_the_book)) {
        val progressText = "$progress%"
        binding.procent.text = progressText
    }

    fun showSuccessBlock(action: () -> Unit) = with(binding) {
        successBlock.isVisible = true
        uploadBlock.isVisible = false
        continueBtn.setOnDownEffectClickListener { action() }
    }

    private fun initAndApplyArguments() {
        if (arguments == null) return
        val title = arguments?.getString(TITLE_KEY)
        val isCancelableFromArgument = arguments?.getBoolean(IS_CANCELABLE_KEY)
        isCancelable = isCancelableFromArgument ?: false
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