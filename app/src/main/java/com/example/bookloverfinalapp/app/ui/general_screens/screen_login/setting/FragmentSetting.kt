package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.modalPageNavigateTo
import com.example.bookloverfinalapp.databinding.FragmentLoginSettingBinding
import com.joseph.ui_core.custom.modal_page.ModalPage

class FragmentSetting : Fragment(), OnClickListener {

    private val binding: FragmentLoginSettingBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentLoginSettingBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
    }

    private fun setOnClickListeners() = with(binding) {
        languageSetting.setOnClickListener(this@FragmentSetting)
        themeSetting.setOnClickListener(this@FragmentSetting)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.theme_setting -> navigateToSelectionFragment(
                SettingSelectionType.THEME_SETTING,
                getString(R.string.theme)
            )
            R.id.language_setting -> navigateToSelectionFragment(
                SettingSelectionType.LANGUAGE_SETTING,
                getString(R.string.language)
            )

        }
    }

    private fun navigateToSelectionFragment(selectionType: SettingSelectionType, title: String) {
        val newFragment = SettingSelectionFragment.newInstance(selectionType)
        modalPageNavigateTo(
            newFragment = newFragment,
            title = getString(R.string.class_setup),
            rootContainer = binding.rootContainer,
            transitionName = binding.rootContainer.transitionName
        )
        binding.root.translationX = -resources.displayMetrics.widthPixels.toFloat()
    }

    companion object {
        fun newInstance(title: String) = FragmentSetting().run {
            ModalPage.Builder()
                .fragment(this)
                .title(title)
                .build()
        }
    }
}