package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.joseph.ui.core.R
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
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center,
    ) {

        Spacer(modifier = modifier.weight(1f))
        Column(
            modifier = modifier
                .padding(start = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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

        val alpha = if (refreshButtonIsVisible && !isFullScreenMode) 1f else 0f
        Spacer(modifier = modifier.weight(1f))
        IconButton(
            modifier = modifier
                .alpha(alpha),
            onClick = refreshButtonOnClick,
        ) {
            Icon(
                modifier = modifier
                    .size(44.dp)
                    .padding(end = 20.dp),
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
    Box(
        modifier = modifier,
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .border(
                    3.dp,
                    color = borderColor,
                    CircleShape
                )
                .size(animatedSizeDp)
                .clip(CircleShape)
                .size(animatedSizeDp)
                .clickable {
                    showAvatarToFulScreenListener()
                }
        )

        AnimatedVisibility(
            modifier = modifier
                .align(Alignment.BottomEnd)
                .padding(top = 32.dp),
            visible = isFullScreenMode,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                onClick = {

                }) {
                Icon(
                    modifier = modifier
                        .size(40.dp),
                    painter = painterResource(id = R.drawable.edit_icon),
                    tint = colorResource(id = R.color.gray_yandex),
                    contentDescription = null,
                )

            }
        }
    }

}
