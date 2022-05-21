package com.example.bookloverfinalapp.app.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.bookloverfinalapp.app.utils.cons.AUTO_LANGUAGE
import com.example.bookloverfinalapp.app.utils.cons.KEY_APP_MODE
import com.example.bookloverfinalapp.app.utils.cons.KEY_DEFAULT_LANGUAGE
import com.example.bookloverfinalapp.app.utils.extensions.dataStore
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object SettingManager {

    fun setAppSetting(scope: CoroutineScope, context: Context) {
        scope.launch {
            context.dataStore.data.collect { pref ->
                val lang = pref[stringPreferencesKey(KEY_DEFAULT_LANGUAGE)]
                setAppLanguage(lang ?: "auto", context = context)
                val currentMode = pref[booleanPreferencesKey(KEY_APP_MODE)] ?: false
                setAppMode(currentMode = currentMode)
            }
        }
    }

    fun setAppLanguage(lang: String, context: Context) {
        if (lang.equals(AUTO_LANGUAGE, true)) Lingver.getInstance().setFollowSystemLocale(context)
        else Lingver.getInstance().setLocale(context, lang)
    }

    fun setAppMode(currentMode: Boolean) {
        if (currentMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}