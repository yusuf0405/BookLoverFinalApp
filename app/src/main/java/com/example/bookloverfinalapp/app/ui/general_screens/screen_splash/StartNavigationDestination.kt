package com.example.bookloverfinalapp.app.ui.general_screens.screen_splash

sealed class StartNavigationDestination {

    object NavigateToLoginScreen: StartNavigationDestination()

    object NavigateToMainScreen : StartNavigationDestination()

    object NavigateToAdminScreen : StartNavigationDestination()

    object NavigateToAccountHasDeletedScreen : StartNavigationDestination()
}