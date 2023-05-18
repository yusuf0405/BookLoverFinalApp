package com.joseph.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.joseph.profile.R

@Composable
fun MotionLayoutExample() {


}

@OptIn(ExperimentalMotionApi::class)
@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier,
    progress: Float
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources.openRawResource(R.raw.motion_scene).readBytes().decodeToString()
    }
    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(Color.Blue)
                .layoutId(layoutId = "box")
        ) {

            Image(
                painter = painterResource(id = R.drawable.avatar_01),
                contentDescription = null,
                modifier = modifier
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = Color.Green,
                        shape = CircleShape
                    )
                    .layoutId(layoutId = "profile_pic")
            )

            Text(
                text = "Joseph Barbera",
                fontSize = 24.sp,
                modifier = modifier.layoutId("username")
            )

        }

    }

}