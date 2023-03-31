package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.cons.KEY_APP_MODE
import com.example.bookloverfinalapp.app.utils.cons.KEY_DEFAULT_LANGUAGE
import com.joseph.utils_core.extensions.dataStore
import com.example.bookloverfinalapp.databinding.FragmentSettingSelectionBinding
import com.joseph.ui_core.extensions.launchOnViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingSelectionFragment : Fragment(), SettingSelectionAdapter.OnItemSelectionListener {

    companion object {
        private const val TYPE = "type"
        const val ORIENTATION_KEY = "ORIENTATION_KEY"
        fun newInstance(type: SettingSelectionType) = SettingSelectionFragment().apply {
            arguments = bundleOf(TYPE to type.ordinal)
        }
    }

    private val binding: FragmentSettingSelectionBinding by lazy(LazyThreadSafetyMode.NONE) {
        FragmentSettingSelectionBinding.inflate(layoutInflater)
    }


    private val type: SettingSelectionType by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionType.valueOf(
            arguments?.getInt(TYPE) ?: SettingSelectionType.ORIENTATION_SETTING.ordinal
        )
    }

    private val adapter: SettingSelectionAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SettingSelectionAdapter(onItemSelectionListener = this)
    }

    private var currentLanguage = String()
    private var currentIsDarkMode = false
    private var orientation = 0

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
        launchOnViewLifecycle {
            requireContext().dataStore.data.collectLatest { pref ->
                currentLanguage = pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)] ?: ""
                orientation = pref[intPreferencesKey(ORIENTATION_KEY)] ?: 1
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
            SettingSelectionType.ORIENTATION_SETTING -> {
                when (orientation) {
                    0 -> getString(R.string.horizontal)
                    1 -> getString(R.string.vertical)
                    else -> ""
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
            SettingSelectionType.THEME_SETTING -> handleAppLanguage(item.title)
            SettingSelectionType.ORIENTATION_SETTING -> handleOrientationSetting(item.title)
        }
    }

    private fun handleOrientationSetting(isCheckedTheme: String) = when (isCheckedTheme) {
        getString(R.string.horizontal) -> setOrientationSetting(0)
        getString(R.string.vertical) -> setOrientationSetting(1)
        else -> Job()
    }

    private fun handleAppLanguage(isCheckedTheme: String) = when (isCheckedTheme) {
        getString(R.string.night_mode) -> setAppTheme(isNightMode = true)
        getString(R.string.light_mode) -> setAppTheme(isNightMode = false)
        else -> Job()
    }

    private fun setOrientationSetting(orientation: Int) = lifecycleScope.launch {
        requireContext().dataStore.edit { pref ->
            pref[intPreferencesKey(ORIENTATION_KEY)] = orientation
        }
    }

    private fun setAppTheme(isNightMode: Boolean) = lifecycleScope.launch {
        if (isNightMode == currentIsDarkMode) return@launch
        requireContext().dataStore.edit { pref ->
            pref[booleanPreferencesKey(KEY_APP_MODE)] = isNightMode
        }
    }

    private fun setLanguageSetting(typeKey: String) = lifecycleScope.launch {
        val newLanguage = LanguageTypes.checkAndGetLanguageType(typeKey = typeKey).name
        if (newLanguage == currentLanguage) return@launch
        requireContext().dataStore.edit { pref ->
            pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)] = newLanguage
        }
    }
}
