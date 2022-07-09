package com.example.bookloverfinalapp.app.ui.general_screens.screen_setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.bookloverfinalapp.R
import com.example.bookloverfinalapp.app.utils.cons.*
import com.example.bookloverfinalapp.app.utils.extensions.dataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FragmentSetting : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val languagePreference =
            preferenceManager.findPreference<Preference>(SETTING_LANGUAGE_KEY) as ListPreference
        val modePreference =
            preferenceManager.findPreference<Preference>(SETTING_THEME_KEY) as SwitchPreference

        modePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                lifecycleScope.launch {
                    requireContext().dataStore.edit { pref ->
                        pref[booleanPreferencesKey(KEY_APP_MODE)] = newValue as Boolean
                    }
                }
                true
            }

        languagePreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                lifecycleScope.launch {
                    requireContext().dataStore.edit { pref ->
                        pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)] = newValue as String
                    }
                }
                true
            }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}