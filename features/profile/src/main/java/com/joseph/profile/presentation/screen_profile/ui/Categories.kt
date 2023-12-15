package com.joseph.profile.presentation.screen_profile.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseph.ui.core.R


@Composable
internal fun Categories(
    modifier: Modifier = Modifier,
    title: String = String(),
    description: String = String(),
    @DrawableRes iconId: Int,
    tint: Color = MaterialTheme.colors.onBackground,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(40.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(
                id = iconId
            ),
            contentDescription = null,
            tint = tint
        )
        Column(
            modifier = modifier.padding(
                start = 16.dp
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontSize = 13.sp,
                color = tint
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.overline,
                    color = if (tint == MaterialTheme.colors.onBackground) Color.Gray else tint
                )
            }
        }
        Spacer(
            modifier = modifier
                .weight(1f)
                .fillMaxHeight()
        )
        Icon(
            painter = painterResource(
                id = R.drawable.right_icon
            ),
            contentDescription = null,
            modifier = modifier
                .align(Alignment.CenterVertically)
                .size(15.dp),
            tint = Color.Gray
        )
    }
}

@Composable
internal fun CategoriesSwitch(
    modifier: Modifier = Modifier,
    title: String = String(),
    description: String = String(),
    isDarkTheme: Boolean = false,
    @DrawableRes iconId: Int,
    tint: Color = MaterialTheme.colors.onBackground,
    onCheckedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp
            )
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(
                id = iconId
            ),
            contentDescription = null,
            tint = tint
        )
        Column(
            modifier = modifier.padding(
                start = 16.dp
            )
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                fontSize = 13.sp,
                color = tint
            )
            if (description.isNotEmpty()) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.overline,
                    color = if (tint == MaterialTheme.colors.onBackground) Color.Gray else tint
                )
            }
        }
        Spacer(
            modifier = modifier
                .weight(1f)
                .fillMaxHeight()
        )
        val mRemember = remember { mutableStateOf(isDarkTheme) }
        Switch(
            checked = mRemember.value,
            onCheckedChange = {
                onCheckedChange(it)
                mRemember.value = it
            },
            enabled = true,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colorResource(id = R.color.blue),
                uncheckedThumbColor = colorResource(id = R.color.gray_silver)
            ),
        )
    }
}