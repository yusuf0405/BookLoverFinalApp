package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.joseph.ui.core.R as UiCore
import com.example.bookloverfinalapp.R
import com.joseph.core.bindingLifecycleError
import com.example.bookloverfinalapp.app.utils.modalPageNavigateTo
import com.example.bookloverfinalapp.databinding.FragmentLoginSettingBinding
import com.joseph.ui.core.custom.modal_page.ModalPage

class FragmentSetting : Fragment(), OnClickListener {

    private var _binding: FragmentLoginSettingBinding? = null
    val binding get() = _binding ?: bindingLifecycleError()

    private var isBookReaderSettingShow = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginSettingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setOnClickListeners()
    }

    private fun setupViews() = with(binding) {
        languageSetting.isVisible = !isBookReaderSettingShow
        orientationSetting.isVisible = isBookReaderSettingShow
    }

    private fun setOnClickListeners() = with(binding) {
        languageSetting.setOnClickListener(this@FragmentSetting)
        themeSetting.setOnClickListener(this@FragmentSetting)
        orientationSetting.setOnClickListener(this@FragmentSetting)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.theme_setting -> navigateToSelectionFragment(
                SettingSelectionType.THEME_SETTING,
                getString(UiCore.string.theme)
            )
            R.id.language_setting -> navigateToSelectionFragment(
                SettingSelectionType.LANGUAGE_SETTING,
                getString(UiCore.string.language)
            )
            R.id.orientation_setting -> navigateToSelectionFragment(
                SettingSelectionType.ORIENTATION_SETTING,
                getString(UiCore.string.orientation)
            )
        }
    }

    private fun navigateToSelectionFragment(selectionType: SettingSelectionType, title: String) {
        val newFragment = SettingSelectionFragment.newInstance(selectionType)
        modalPageNavigateTo(
            newFragment = newFragment,
            title = title,
            rootContainer = binding.rootContainer,
            transitionName = binding.rootContainer.transitionName
        )
        binding.root.translationX = -resources.displayMetrics.widthPixels.toFloat()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            title: String,
            isBookReaderSettingShow: Boolean = false,
        ) = FragmentSetting().run {
            this.isBookReaderSettingShow = isBookReaderSettingShow
            ModalPage.Builder()
                .fragment(this)
                .title(title)
                .build()
        }
    }
}