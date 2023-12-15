package com.joseph.ui.core

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val ShimmerColorShades = listOf(

    Color.LightGray.copy(0.9f),

    Color.LightGray.copy(0.2f),

    Color.LightGray.copy(0.9f)

)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColors(
            primary = Color(0xFF1772FF),
            primaryVariant = DARK3,
            secondary = colorResource(id = R.color.white),
            surface = DARK2,
            background = colorResource(id = R.color.rating_background),
            onSecondary = colorResource(id = R.color.rating_first_background),
            error = colorResource(id = R.color.text_dark_negative)
        )
    } else {
        lightColors(
            primary = colorResource(id = R.color.blue),
            secondary = colorResource(id = R.color.black_night),
            primaryVariant = Color(0xFF3700B3),
            background = colorResource(id = R.color.cardBackground),
            onSecondary = colorResource(id = R.color.text_inverted),
            error = colorResource(id = R.color.widget_icon_background)
        )
    }
    val typography = Typography(
        h1 = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        ),
        h2 = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
            fontWeight = FontWeight.Normal,
            fontSize = 19.sp
        ),
        h5 = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.15.sp
        ),

        h6 = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = 0.15.sp
        ),
        caption = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins)),
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = 0.15.sp
        ),
        overline = TextStyle(
            fontFamily = FontFamily(Font(R.font.poppins)),
            fontWeight = FontWeight.Normal,
            fontSize = 11.sp,
            letterSpacing = 0.15.sp
        ),
        body1 = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        )
    )
    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(0.dp)
    )

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

val DARK1 = Color(red = 22, green = 23, blue = 29)
val DARK2 = Color(red = 28, green = 29, blue = 35)
val DARK3 = Color(red = 31, green = 31, blue = 36)
val DARK4 = Color(red = 34, green = 35, blue = 48)
val Red = Color(red = 220, green = 0, blue = 59)
