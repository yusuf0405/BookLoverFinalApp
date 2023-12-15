package com.example.bookloverfinalapp.app.ui.general_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.joseph.core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.runWithArgumentsOrSkip
import com.joseph.core.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.ProgressDialogBinding

class ProgressDialog : DialogFragment() {

    private var _binding: ProgressDialogBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ProgressDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tuneLyricsDialog()
        initAndApplyArguments()
    }

    private fun initAndApplyArguments() = runWithArgumentsOrSkip { arguments ->
        val title = arguments.getString(TITLE_KEY)
        val isCancelableFromArgument = arguments.getBoolean(IS_CANCELABLE_KEY)
        isCancelable = isCancelableFromArgument
        if (title.isNullOrEmpty()) return@runWithArgumentsOrSkip
        binding.titleText.text = title
    }

    companion object {
        private const val TITLE_KEY = "TITLE_KEY"
        private const val IS_CANCELABLE_KEY = "IS_CANCELABLE_KEY"

        fun getInstance(title: String = String(), isCancelable: Boolean = false): ProgressDialog {
            val dialog = ProgressDialog()
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, title)
            bundle.putBoolean(IS_CANCELABLE_KEY, isCancelable)
            dialog.arguments = bundle
            return dialog
        }
    }
}