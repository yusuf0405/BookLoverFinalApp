package com.joseph.profile.presentation.screen_edit_profile

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import coil.compose.AsyncImage
import com.joseph.profile.domain.models.EditProfileColors
import com.joseph.profile.domain.models.EditProfileColorsState
import com.joseph.profile.presentation.ProfileHeader
import com.joseph.profile.presentation.screen_edit_profile.ui.AllTextFiledBlock
import com.joseph.profile.presentation.screen_edit_profile.ui.EditProfileToolbar
import com.joseph.profile.presentation.screen_edit_profile.ui.UserAvatarEndEmail
import com.joseph.ui_core.R


@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    refreshButtonOnClick: () -> Unit,
    editProfileUiSate: EditProfileUiSate,
    profileAppBarColor: MutableState<EditProfileColors>,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onNameValueChange: (String) -> Unit,
    onLastNameValueChange: (String) -> Unit,
    updateButtonOnClickListener: () -> Unit
) {
    val scrollState = rememberScrollState()
    val lazyScrollState = rememberLazyListState()


    Log.i("Joseph", "rememberScrollState = ${scrollState.maxValue}")
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(
                horizontal = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        var buttonEnabled by remember { mutableStateOf(false) }
        buttonEnabled = editProfileUiSate.buttonEnabled

        var isShowFullScreenMode by remember { mutableStateOf(false) }

        val isNeedExpansion = rememberSaveable { mutableStateOf(false) }

        val animatedSizeDp: Dp by animateDpAsState(targetValue = if (isNeedExpansion.value) 400.dp else 100.dp)

        val editProfileColor = when (editProfileUiSate.editProfileColorsState) {
            EditProfileColorsState.EditProfileDefaultState -> MaterialTheme.colors.secondary
            EditProfileColorsState.EditProfileErrorState -> MaterialTheme.colors.error
            EditProfileColorsState.EditProfileSuccessState -> colorResource(id = R.color.blue)
        }
//        val progress by animateFloatAsState(
//            targetValue = if (scrollState.value in 0..1) 0f else 1f,
//            tween(500)
//        )
//        val motionHeight by animateDpAsState(
//            targetValue = if (scrollState.firstVisibleItemIndex in 0..1) 230.dp else 56.dp,
//            tween(500)
//        )
        var progress by remember {
            mutableStateOf(0f)
        }
//        ProfileHeader(progress = progress)
//        Slider(
//            value = progress,
//            onValueChange = {
//                progress = it
//            })
//        if (!isNeedExpansion.value) {
//            EditProfileToolbar(
//                navigateBack = navigateBack,
//                refreshButtonOnClick = refreshButtonOnClick,
//                editProfileColor = editProfileColor,
//                refreshButtonIsVisible = editProfileUiSate.refreshButtonIsVisible
//            )
//
//        }
        editProfileUiSate.firstName
        UserAvatarEndEmail(
            borderColor = editProfileColor,
            editProfileUiSate = editProfileUiSate,
            animatedSizeDp = animatedSizeDp,
            isFullScreenMode = isNeedExpansion.value,
            refreshButtonOnClick = refreshButtonOnClick,
            refreshButtonIsVisible = editProfileUiSate.refreshButtonIsVisible,
            showAvatarToFulScreenListener = {
                isNeedExpansion.value = !isNeedExpansion.value
            }
        )

        if (!isNeedExpansion.value) {
            AllTextFiledBlock(
                onEmailValueChange = onEmailValueChange,
                onNameValueChange = onNameValueChange,
                onLastNameValueChange = onLastNameValueChange,
                onPasswordValueChange = onPasswordValueChange,
                editProfileUiSate = editProfileUiSate,
            )

            Button(
                onClick = {
                    updateButtonOnClickListener()
                },
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
                    .height(50.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = editProfileColor,
                    disabledBackgroundColor = colorResource(id = R.color.gray_yandex)
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 16.dp
                ),
                enabled = buttonEnabled
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.edit_icon),
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.update_profile),
                    style = MaterialTheme.typography.h5,
                    modifier = modifier.padding(start = 4.dp),
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
fun FulScreenUserAvatar(
    modifier: Modifier = Modifier,
    avatarUrl: String,
    hideFullScreenModeListener: () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable { hideFullScreenModeListener() }
            .fillMaxWidth()
            .fillMaxHeight()
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(400.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        )
    }
}