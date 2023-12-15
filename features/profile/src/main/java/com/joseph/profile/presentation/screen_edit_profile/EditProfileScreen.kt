package com.joseph.profile.presentation.screen_edit_profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.joseph.profile.domain.models.EditProfileColors
import com.joseph.profile.domain.models.EditProfileColorsState
import com.joseph.profile.presentation.screen_edit_profile.ui.AllTextFiledBlock
import com.joseph.profile.presentation.screen_edit_profile.ui.UserAvatarEndEmail
import com.joseph.ui.core.MyApplicationTheme
import com.joseph.ui.core.R

@Preview
@Composable
fun EditProfileScreenPreview() {
    MyApplicationTheme() {
        EditProfileScreen(
            profileAppBarColor = remember { mutableStateOf(EditProfileColors.DEFAULT) },
            editProfileUiSate = EditProfileUiSate.preview
        )
    }
}

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {},
    refreshButtonOnClick: () -> Unit = {},
    editProfileUiSate: EditProfileUiSate,
    profileAppBarColor: MutableState<EditProfileColors>,
    onEmailValueChange: (String) -> Unit = {},
    onPasswordValueChange: (String) -> Unit = {},
    onNameValueChange: (String) -> Unit = {},
    onLastNameValueChange: (String) -> Unit = {},
    updateButtonOnClickListener: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val isShowFullScreenMode = rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (isShowFullScreenMode.value) Arrangement.Center else Arrangement.Top
    ) {
        var buttonEnabled by remember { mutableStateOf(false) }
        buttonEnabled = editProfileUiSate.buttonEnabled

        val animatedSizeDp: Dp by animateDpAsState(
            targetValue = if (isShowFullScreenMode.value) 250.dp else 100.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessLow,
            )
        )

        val editProfileColor = when (editProfileUiSate.editProfileColorsState) {
            EditProfileColorsState.EditProfileDefaultState -> MaterialTheme.colors.secondary
            EditProfileColorsState.EditProfileErrorState -> MaterialTheme.colors.error
            EditProfileColorsState.EditProfileSuccessState -> MaterialTheme.colors.primary
        }

        UserAvatarEndEmail(
            modifier = modifier,
            borderColor = editProfileColor,
            editProfileUiSate = editProfileUiSate,
            animatedSizeDp = animatedSizeDp,
            isFullScreenMode = isShowFullScreenMode.value,
            refreshButtonOnClick = refreshButtonOnClick,
            refreshButtonIsVisible = editProfileUiSate.refreshButtonIsVisible,
            showAvatarToFulScreenListener = {
                isShowFullScreenMode.value = !isShowFullScreenMode.value
            }
        )

        AnimatedVisibility(
            visible = !isShowFullScreenMode.value,
            enter = slideInVertically() + fadeIn(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                ),
                initialAlpha = 0.3f
            ) + expandVertically(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutSlowInEasing
                ),
                expandFrom = Alignment.Bottom,
            ),
            exit = slideOutVertically(
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = FastOutLinearInEasing
                )
            ) + shrinkVertically() + fadeOut(
                animationSpec = tween(
                    durationMillis = 0,
                    easing = FastOutLinearInEasing,
                )
            )
        ) {

            Column(
                modifier = modifier.padding(horizontal = 16.dp)
            ) {
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
                        .height(60.dp)
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
                    Text(
                        text = stringResource(id = R.string.update_profile),
                        style = MaterialTheme.typography.h5,
                        color = Color.White,
                    )
                }
            }
        }
    }
}
