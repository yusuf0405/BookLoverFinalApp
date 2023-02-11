package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.login_out

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.utils.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.app.utils.extensions.tuneBottomDialog
import com.example.bookloverfinalapp.app.utils.extensions.tuneLyricsDialog
import com.example.bookloverfinalapp.databinding.FragmentLoginOutDialogBinding
import com.joseph.ui_core.custom.modal_page.ModalPage
import com.joseph.ui_core.custom.modal_page.dismissModalPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLoginOutDialog : DialogFragment() {

    private var _binding: FragmentLoginOutDialogBinding? = null
    private val binding get() = _binding ?: bindingLifecycleError()

    private val viewModel: FragmentLoginOutDialogViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginOutDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        loginOutButton.setOnDownEffectClickListener {
            viewModel.loginOut()
            dismissModalPage()
        }
        cancelButton.setOnDownEffectClickListener {
            dismissModalPage()
        }
    }

    companion object {
        fun newInstance() = FragmentLoginOutDialog().run {
            ModalPage.Builder()
                .fragment(this)
                .build()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}