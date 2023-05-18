package com.joseph.profile.presentation.screen_profile.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joseph.profile.R

@Composable
internal fun ProfileTextHeader(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(id = R.string.account),
        style = MaterialTheme.typography.h1,
        modifier = modifier.padding(
            start = 16.dp
        )
    )
}