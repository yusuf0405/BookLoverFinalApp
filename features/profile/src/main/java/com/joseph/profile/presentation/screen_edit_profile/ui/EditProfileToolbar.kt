package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joseph.ui_core.R

@Composable
internal fun EditProfileToolbar(
    modifier: Modifier = Modifier,
    editProfileColor: Color,
    navigateBack: () -> Unit,
    refreshButtonOnClick: () -> Unit,
    refreshButtonIsVisible: Boolean
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = modifier
                .padding(top = 32.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = navigateBack,
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.arrow_left
                    ),
                    tint = editProfileColor,
                    contentDescription = null,
                )
            }
            Text(
                text = stringResource(id = R.string.bio_data),
                style = MaterialTheme.typography.h6.copy(color = editProfileColor),
                textAlign = TextAlign.Center,
            )

            if (refreshButtonIsVisible) {
                IconButton(
                    modifier = modifier.size(40.dp),
                    onClick = refreshButtonOnClick,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        tint = editProfileColor,
                        contentDescription = null,
                    )
                }
            } else {
                Spacer(modifier = modifier.size(40.dp))
            }
        }
    }
}