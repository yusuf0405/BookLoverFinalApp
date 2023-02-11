package com.example.bookloverfinalapp.app.ui.general_screens.screen_login.setting

enum class Navigation {
    FORWARD,
    BACK;

    companion object {
        fun valueOf(value: Int) = when (value) {
            FORWARD.ordinal -> FORWARD
            else -> BACK
        }
    }
}