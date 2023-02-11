package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import android.content.Context
import com.example.bookloverfinalapp.R


enum class SettingSelectionType {
    THEME_SETTING,
    LANGUAGE_SETTING;

    companion object {
        fun valueOf(value: Int) = when (value) {
            THEME_SETTING.ordinal -> THEME_SETTING
            LANGUAGE_SETTING.ordinal -> LANGUAGE_SETTING
            else -> THEME_SETTING
        }
    }

    fun selectionItems(context: Context): ArrayList<String> =
        when (this) {
            THEME_SETTING -> arrayListOf(
                context.getString(R.string.night_mode),
                context.getString(R.string.light_mode),
            )
            LANGUAGE_SETTING -> arrayListOf(
                context.getString(R.string.english),
                context.getString(R.string.russian),
                context.getString(R.string.Kyrgyz),
                context.getString(R.string.Uzbek),
            )
        }

}