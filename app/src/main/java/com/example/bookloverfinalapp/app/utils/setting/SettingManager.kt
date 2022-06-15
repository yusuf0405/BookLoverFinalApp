package com.example.bookloverfinalapp.app.utils.setting

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.preference.PreferenceManager
import com.example.bookloverfinalapp.app.utils.cons.*
import com.example.bookloverfinalapp.app.utils.extensions.dataStore
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

object SettingManager {

    fun setAppSetting(scope: CoroutineScope, context: Context) {
        scope.launch {
            context.dataStore.data.collectLatest { pref ->
                val lang = pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)]
                setAppLanguage(lang ?: SETTING_LANGUAGE_AUTO_KEY, context = context)
                val currentMode = pref[booleanPreferencesKey(KEY_APP_MODE)] ?: false
                setAppMode(currentMode = currentMode)
            }
        }
    }

    private fun setAppLanguage(lang: String, context: Context) {
        if (lang.equals(AUTO_LANGUAGE, true)) Lingver.getInstance().setFollowSystemLocale(context)
        else Lingver.getInstance().setLocale(context, lang)
    }

    private fun setAppMode(currentMode: Boolean) {
        if (currentMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    @SuppressLint("CommitPrefEdits")
    fun clearAppSettings(scope: CoroutineScope, context: Context) {
        scope.launch {
            context.dataStore.edit { pref ->
                pref[booleanPreferencesKey(KEY_APP_MODE)] = false
                pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)] = "auto"
                PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                    putBoolean(SETTING_THEME_KEY, false).apply()
                    putString(SETTING_LANGUAGE_KEY, SETTING_LANGUAGE_AUTO_KEY).apply()
                }

            }
        }
    }

}