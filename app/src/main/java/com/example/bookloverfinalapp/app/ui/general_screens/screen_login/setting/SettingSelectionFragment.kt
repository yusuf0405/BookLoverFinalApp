package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.cons.KEY_APP_MODE
import com.example.bookloverfinalapp.app.utils.cons.KEY_DEFAULT_LANGUAGE
import com.example.bookloverfinalapp.app.utils.extensions.dataStore
import com.example.bookloverfinalapp.app.utils.setting.SettingManager
import com.example.bookloverfinalapp.databinding.FragmentSettingSelectionBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SettingSelectionFragment : Fragment(), SettingSelectionAdapter.OnItemSelectionListener {

    companion object {
        private const val TYPE = "type"

        fun newInstance(type: SettingSelectionType) = SettingSelectionFragment().apply {
            arguments = bundleOf(TYPE to type.ordinal)
        }
    }

    private val binding: FragmentSettingSelectionBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSettingSelectionBinding.inflate(layoutInflater)
    }

    private val type: SettingSelectionType by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionType.valueOf(
            arguments?.getInt(TYPE) ?: SettingSelectionType.THEME_SETTING.ordinal
        )
    }

    private val adapter: SettingSelectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionAdapter(onItemSelectionListener = this)
    }

    private var currentLanguage = String()
    private var currentIsDarkMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = ModalPageNavigationTransition(Navigation.BACK)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (savedInstanceState == null) {
            binding.root.translationX = resources.displayMetrics.widthPixels.toFloat()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        getAppSettings()
        setupAppSettings()
    }

    private fun getAppSettings() {
        lifecycleScope.launch {
            requireContext().dataStore.data.collectLatest { pref ->
                currentLanguage = pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)] ?: ""
                currentIsDarkMode = pref[booleanPreferencesKey(KEY_APP_MODE)] ?: false
            }
        }
    }

    private fun setupAppSettings() = with(requireContext()) {
        val checkedItem = when (type) {
            SettingSelectionType.THEME_SETTING -> {
                if (currentIsDarkMode) getString(R.string.night_mode)
                else getString(R.string.light_mode)
            }
            SettingSelectionType.LANGUAGE_SETTING -> {
                when (currentLanguage) {
                    LanguageTypes.ru.name -> getString(R.string.russian)
                    LanguageTypes.en.name -> getString(R.string.english)
                    LanguageTypes.uz.name -> getString(R.string.Uzbek)
                    LanguageTypes.ky.name -> getString(R.string.Kyrgyz)
                    else -> String()
                }
            }
        }
        populateAdapterModels(checkedItem)
    }

    private fun populateAdapterModels(checkedItem: String) {
        val textItems = type.selectionItems(requireContext())
        val selectionItems =
            textItems.map { SettingSelectionItem(title = it, isChecked = it == checkedItem) }
        adapter.populate(selectionItems)
    }

    override fun onItemSelected(item: SettingSelectionItem) {
        when (type) {
            SettingSelectionType.LANGUAGE_SETTING -> setLanguageSetting(item.title)
            SettingSelectionType.THEME_SETTING -> handleAppLanguage(isCheckedTheme = item.title)
        }
    }

    private fun handleAppLanguage(isCheckedTheme: String) = with(requireContext()) {
        when (isCheckedTheme) {
            getString(R.string.night_mode) -> setAppTheme(isNightMode = true)
            getString(R.string.light_mode) -> setAppTheme(isNightMode = false)
        }
    }

    private fun setAppTheme(isNightMode: Boolean) {
        if (isNightMode == currentIsDarkMode) return
        lifecycleScope.launch {
            requireContext().dataStore.edit { pref ->
                pref[booleanPreferencesKey(KEY_APP_MODE)] = isNightMode
            }
        }
    }

    private fun setLanguageSetting(typeKey: String) {
        val newLanguage = LanguageTypes.checkAndGetLanguageType(typeKey = typeKey).name
        if (newLanguage == currentLanguage) return
        lifecycleScope.launch {
            requireContext().dataStore.edit { pref ->
                pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)] = newLanguage
            }
        }
    }
}