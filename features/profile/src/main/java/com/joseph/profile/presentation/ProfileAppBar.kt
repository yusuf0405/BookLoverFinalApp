package com.joseph.profile.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.joseph.profile.domain.models.EditProfileColorsState
import com.joseph.profile.presentation.navigation.Destinations
import com.joseph.ui.core.R


@Composable
fun ProfileAppBar(
    modifier: Modifier = Modifier,
    canNavigationBack: Boolean,
    currentScreen: Destinations,
    profileAppBarColor: EditProfileColorsState,
    onNavigateBack: () -> Unit,
) {
    var color = MaterialTheme.colors.onSecondary
    var textColor = MaterialTheme.colors.onSurface
    when (profileAppBarColor) {
        EditProfileColorsState.EditProfileDefaultState -> {
            color = MaterialTheme.colors.onSecondary
            textColor = MaterialTheme.colors.onSurface
        }

        EditProfileColorsState.EditProfileErrorState -> {
            color = MaterialTheme.colors.error
            textColor = Color.White
        }

        EditProfileColorsState.EditProfileSuccessState -> {
            color = colorResource(id = R.color.blue)
            textColor = Color.White
        }
    }
    if (!canNavigationBack) {
        color = MaterialTheme.colors.onSecondary
        textColor = MaterialTheme.colors.onSurface
    }

    Surface(
        modifier = modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(60.dp),
        elevation = 4.dp,
        color = color
    ) {
        Row(
            modifier = modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = canNavigationBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = textColor
                    )
                }
                Spacer(modifier = modifier.width(24.dp))
            }
            Text(
                text = currentScreen.title,
                style = MaterialTheme.typography.h1,
                modifier = modifier.padding(12.dp),
                color = textColor
            )
        }
    }
}