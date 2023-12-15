package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.login_out

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.bookloverfinalapp.app.base.BaseBindingFragment
import com.example.bookloverfinalapp.app.utils.extensions.setOnDownEffectClickListener
import com.example.bookloverfinalapp.databinding.FragmentLoginOutDialogBinding
import com.joseph.ui.core.custom.modal_page.ModalPage
import com.joseph.ui.core.custom.modal_page.dismissModalPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLoginOutDialog :
    BaseBindingFragment<FragmentLoginOutDialogBinding>(FragmentLoginOutDialogBinding::inflate) {

    private val viewModel: FragmentLoginOutDialogViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding()) {
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
}