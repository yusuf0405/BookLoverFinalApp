package com.joseph.profile.presentation.screen_profile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.joseph.profile.domain.models.UserFeatureModel
import com.joseph.ui.core.R

@Composable
internal fun ProfileInfo(
    modifier: Modifier = Modifier,
    user: UserFeatureModel,
    navigateToEditProfile: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.primary,
                elevation = 8.dp,
        modifier = modifier
            .padding(
                all = 16.dp,
            )
            .height(89.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.avatar_06
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .padding
                        (
                        start = 12.dp
                    )
                    .size(53.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colors.background, CircleShape)
            )
            Column(
                modifier = modifier
                    .padding(
                        start = 16.dp
                    ),
            ) {
                Text(
                    text = user.fullName(),
                    style = MaterialTheme.typography.h6,
                    color = Color.White
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.caption,
                    color = Color.White
                )
            }

            Spacer(
                modifier = modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            IconButton(
                onClick = navigateToEditProfile,
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.edit_icon
                    ),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = modifier.size(25.dp)
                )
            }
        }
    }
}
