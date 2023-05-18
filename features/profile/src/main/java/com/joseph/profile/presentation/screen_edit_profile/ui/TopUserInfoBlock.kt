package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.joseph.profile.R
import com.joseph.profile.presentation.screen_edit_profile.EditProfileUiSate


@Composable
internal fun UserAvatarEndEmail(
    modifier: Modifier = Modifier,
    editProfileUiSate: EditProfileUiSate,
    animatedSizeDp: Dp,
    borderColor: Color,
    isFullScreenMode: Boolean,
    showAvatarToFulScreenListener: () -> Unit,
    refreshButtonOnClick: () -> Unit,
    refreshButtonIsVisible: Boolean
) {
    Row(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {

        Spacer(modifier = modifier.height(1.dp))
        Column(
            modifier = modifier
                .weight(1f)
                .padding(start = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserAvatar(
                borderColor = borderColor,
                avatarUrl = editProfileUiSate.userFeatureModel.imageUrl,
                animatedSizeDp = animatedSizeDp,
                isFullScreenMode = isFullScreenMode,
                showAvatarToFulScreenListener = showAvatarToFulScreenListener
            )
            val name = editProfileUiSate.firstName.ifEmpty { stringResource(id = R.string.empty) }
            val lastName =
                editProfileUiSate.lastName.ifEmpty { stringResource(id = R.string.empty) }
            val email = editProfileUiSate.email.ifEmpty { stringResource(id = R.string.empty) }
            if (!isFullScreenMode) {
                Text(
                    text = "$name $lastName",
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }

        val alpha = if (refreshButtonIsVisible) 1f else 0f

        IconButton(
            modifier = modifier
                .size(40.dp)
                .alpha(alpha)
                .padding(start = 20.dp)
                .fillMaxSize(),
            onClick = refreshButtonOnClick,
        ) {
            Icon(
                modifier = modifier
                    .size(32.dp)
                    .fillMaxSize(0.5f),
                imageVector = Icons.Outlined.Info,
                tint = borderColor,
                contentDescription = null,
            )
        }
    }


}

@Composable
fun UserAvatar(
    modifier: Modifier = Modifier,
    borderColor: Color,
    isFullScreenMode: Boolean,
    animatedSizeDp: Dp,
    avatarUrl: String,
    showAvatarToFulScreenListener: () -> Unit,
) {
    val newModifier = if (isFullScreenMode) {
        modifier
            .size(animatedSizeDp)
            .background(Color.Green)
    } else modifier
        .clip(CircleShape)
        .border(
            3.dp,
            color = borderColor,
            CircleShape
        )
    AsyncImage(
        model = avatarUrl,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = newModifier
            .size(animatedSizeDp)
            .clickable {
                showAvatarToFulScreenListener()
            }
    )
}
