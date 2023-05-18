package com.joseph.profile.presentation.screen_profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
internal fun ShimmerProfileInfo(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .height(89.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .padding(start = 16.dp)
                .size(53.dp)
                .clip(CircleShape)
                .background(brush = brush)
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp
                ),
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(
                modifier = Modifier
                    .height(12.dp)
                    .width(150.dp)
                    .background(brush = brush)
            )
            Spacer(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(8.dp)
                    .width(220.dp)
                    .background(brush = brush)
            )
        }
    }

}