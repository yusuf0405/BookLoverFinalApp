package com.joseph.profile.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.joseph.profile.domain.models.EditProfileColors
import com.joseph.profile.domain.models.EditProfileColorsState
import com.joseph.profile.presentation.navigation.EditProfile
import com.joseph.profile.presentation.navigation.Profile
import com.joseph.profile.presentation.navigation.profileDestinations
import com.joseph.profile.presentation.screen_edit_profile.EditProfileScreen
import com.joseph.profile.presentation.screen_edit_profile.EditProfileViewModel
import com.joseph.profile.presentation.screen_profile.FragmentProfileScreenViewModel
import com.joseph.profile.presentation.screen_profile.ProfileScreen
import com.joseph.ui.core.R


@Composable
fun ProfileScreensApp(
    profileScreenViewModel: FragmentProfileScreenViewModel,
    editProfileViewModel: EditProfileViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    navigateSettingScreen: () -> Unit,
    navigateLoginOutScreen: () -> Unit,
) {
    val navController = rememberNavController()
    val systemUiController = rememberSystemUiController()
    val isSystemDark = isSystemInDarkTheme()
    val scaffoldState = rememberScaffoldState()

    var statusBarColor = when (editProfileViewModel.uiState.editProfileColorsState) {
        EditProfileColorsState.EditProfileDefaultState -> MaterialTheme.colors.onSecondary
        EditProfileColorsState.EditProfileErrorState -> MaterialTheme.colors.error
        EditProfileColorsState.EditProfileSuccessState -> colorResource(id = R.color.blue)
    }
    if (navController.previousBackStackEntry == null) {
        statusBarColor = MaterialTheme.colors.onSecondary
    }

    SideEffect {
        systemUiController.setStatusBarColor(color = statusBarColor, darkIcons = !isSystemDark)
    }
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = profileDestinations.find { movieDestination ->
        backStackEntry?.destination?.route == movieDestination.route ||
                backStackEntry?.destination?.route == movieDestination.routeWithArgs
    } ?: Profile

    val profileAppBarColor = remember { mutableStateOf(EditProfileColors.DEFAULT) }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ProfileAppBar(
                canNavigationBack = navController.previousBackStackEntry != null,
                currentScreen = currentScreen,
                profileAppBarColor = editProfileViewModel.uiState.editProfileColorsState
            ) {
                navController.navigateUp()
            }
        }
    ) { innerPaddings ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPaddings),
            startDestination = Profile.routeWithArgs,
        ) {

            composable(route = Profile.routeWithArgs) {
                ProfileScreen(
                    uiState = profileScreenViewModel.uiState,
                    isDarkTheme = isDarkTheme,
                    navigateToEditProfile = { navController.navigate(EditProfile.route) },
                    onThemeChange = onThemeChange,
                    navigateSettingScreen = navigateSettingScreen,
                    navigateLoginOutScreen = navigateLoginOutScreen
                )
            }

            composable(route = EditProfile.routeWithArgs) {
                EditProfileScreen(
                    navigateBack = { navController.navigateUp() },
                    refreshButtonOnClick = editProfileViewModel::refreshUserProfile,
                    editProfileUiSate = editProfileViewModel.uiState,
                    onEmailValueChange = editProfileViewModel::updateUserEmail,
                    onPasswordValueChange = editProfileViewModel::updatePassword,
                    onNameValueChange = editProfileViewModel::updateUserName,
                    onLastNameValueChange = editProfileViewModel::updateUserLastName,
                    updateButtonOnClickListener = editProfileViewModel::updateUserProfile,
                    profileAppBarColor = profileAppBarColor
                )
            }
        }
    }
}

