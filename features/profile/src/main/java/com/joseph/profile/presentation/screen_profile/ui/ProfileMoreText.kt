package com.joseph.profile.presentation.screen_profile.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun ProfileMoreText(
    modifier: Modifier = Modifier
) {
    Text(
        text = "More",
        modifier = modifier.padding(start = 16.dp),
        style = MaterialTheme.typography.h5,
        color = Color.Gray
    )
}