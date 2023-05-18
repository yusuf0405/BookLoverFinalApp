package com.joseph.profile.presentation.screen_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.profile.presentation.screen_profile.ui.ProfileCategories
import com.joseph.profile.presentation.screen_profile.ui.ProfileInfo
import com.joseph.profile.presentation.screen_profile.ui.ProfileMoreCategories
import com.joseph.profile.presentation.screen_profile.ui.ProfileMoreText
import com.joseph.profile.presentation.screen_profile.ui.ShimmerAnimation

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiState: ProfileScreenUiState,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    navigateToEditProfile: () -> Unit,
    navigateSettingScreen: () -> Unit,
    navigateLoginOutScreen: () -> Unit,
) {
    Box {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

        ) {
//            ProfileTextHeader()
            var visible by remember { mutableStateOf(true) }
            visible = !uiState.loading
            if (uiState.loading) {
                ShimmerAnimation()
            }
            AnimationProfileInfo(
                visible = visible,
                user = uiState.user,
                navigateToEditProfile = navigateToEditProfile
            )
            ProfileCategories(
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                navigateSettingScreen = navigateSettingScreen,
                navigateLoginOutScreen = navigateLoginOutScreen
            )
            ProfileMoreText()
            ProfileMoreCategories()
        }
    }
}

@Composable
fun AnimationProfileInfo(
    visible: Boolean,
    user: UserFeatureModel,
    navigateToEditProfile: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(1000)),
        exit = fadeOut(animationSpec = tween(1000))
    ) {
        ProfileInfo(
            user = user,
            navigateToEditProfile = navigateToEditProfile
        )
    }
}