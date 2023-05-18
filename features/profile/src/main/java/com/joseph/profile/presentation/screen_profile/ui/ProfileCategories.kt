package com.joseph.profile.presentation.screen_profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joseph.profile.presentation.screen_profile.ui.Categories
import com.joseph.profile.presentation.screen_profile.ui.CategoriesSwitch
import com.joseph.ui_core.R


@Composable
internal fun ProfileCategories(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    navigateSettingScreen: () -> Unit,
    navigateLoginOutScreen: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.onSecondary,
        elevation = 8.dp,
        modifier = modifier
            .padding(
                all = 16.dp,
            )
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    all = 16.dp
                )
        ) {
            Categories(
                title = stringResource(id = R.string.my_account),
                description = stringResource(id = R.string.make_changes_to_your_account),
                iconId = R.drawable.account_icon,
                onClick = {

                }
            )
            Categories(
                title = stringResource(id = R.string.settings),
                description = stringResource(id = R.string.view_application_settings),
                iconId = R.drawable.setting_categories_icon,
                onClick = {
                    navigateSettingScreen()
                }
            )
            Categories(
                title = "Face ID / Touch ID",
                description = "Manage your device security",
                iconId = R.drawable.lock_icon,
                onClick = {

                }
            )
            CategoriesSwitch(
                title = stringResource(id = R.string.dark_theme),
                description = stringResource(id = R.string.apply_a_dark_theme),
                iconId = R.drawable.authentication_icon,
                isDarkTheme = isDarkTheme,
                onCheckedChange = onThemeChange
            )
            Categories(
                title = stringResource(id = R.string.log_out),
                description = stringResource(id = R.string.further_secure_your_account_for_safety),
                iconId = R.drawable.login_out_icon,
                tint = MaterialTheme.colors.error,
                onClick = {
                    navigateLoginOutScreen()
                }
            )
        }
    }
}