package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

import java.util.UUID

data class SettingSelectionItem(
    var id: String = UUID.randomUUID().toString(),
    var title: String = "",
    var isChecked: Boolean
)

enum class LanguageTypes(val languageKey: String? = null) {
    ru(languageKey = "Русский"),
    en(languageKey = "English"),
    ky(languageKey = "Кыргыз тили"),
    uz(languageKey = "Uzbek tili");

    companion object {
        fun checkAndGetLanguageType(typeKey: String?): LanguageTypes =
            values().find { it.languageKey == typeKey } ?: en
    }
}