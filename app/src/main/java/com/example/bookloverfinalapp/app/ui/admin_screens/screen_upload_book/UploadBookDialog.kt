package com.example.bookloverfinalapp.app.ui.admin_screens.screen_upload_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.runWithArgumentsOrSkip
import com.example.bookloverfinalapp.app.utils.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.DialogUploadBookBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadBookDialog : DialogFragment() {

    private var _binding: DialogUploadBookBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: FragmentUploadFileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogUploadBookBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tuneLyricsDialog()
        initAndApplyArguments()
    }

    fun setTitle(progress: Int, title: String = "Загружаем книгу") {
        binding.title.text = "$title $progress%"
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

        fun getInstance(title: String = String(), isCancelable: Boolean = false): UploadBookDialog {
            val dialog = UploadBookDialog()
            val bundle = Bundle()
            bundle.putString(TITLE_KEY, title)
            bundle.putBoolean(IS_CANCELABLE_KEY, isCancelable)
            dialog.arguments = bundle
            return dialog
        }
    }
}