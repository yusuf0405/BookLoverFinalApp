package com.joseph.profile.presentation.screen_profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joseph.profile.presentation.screen_profile.ui.Categories
import com.joseph.ui_core.R

@Composable
internal fun ProfileMoreCategories(
    modifier: Modifier = Modifier
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
            .height(139.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Categories(
                title = "Help & Support",
                iconId = R.drawable.notification_icon,
                onClick = {

                }
            )
            Categories(
                title = "About App",
                iconId = R.drawable.about_app_icon,
                onClick = {

                }
            )
        }
    }
}