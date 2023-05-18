package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseph.profile.domain.models.UserFieldValidatorState
import com.joseph.profile.domain.models.ValidatorTextColors
import com.joseph.profile.domain.models.ValidatorTextStyle
import com.joseph.ui_core.R

@Composable
internal fun TextFieldEditProfile(
    modifier: Modifier = Modifier,
    text: String,
    imageVector: ImageVector,
    userFieldValidatorState: UserFieldValidatorState,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        shape = CircleShape.copy(CornerSize(size = 5.dp)),
        elevation = 4.dp,
    ) {
        val labelColor = when (userFieldValidatorState.textColor) {
            ValidatorTextColors.DEFAULT -> MaterialTheme.colors.secondary
            ValidatorTextColors.ERROR -> MaterialTheme.colors.error
            ValidatorTextColors.SUCCESS -> colorResource(id = R.color.blue)
        }
        val labelTextStyle = when (userFieldValidatorState.texStyle) {
            ValidatorTextStyle.DEFAULT -> MaterialTheme.typography.caption.copy(fontSize = 15.sp)
            ValidatorTextStyle.LITTLE -> MaterialTheme.typography.caption.copy(fontSize = 11.sp)
        }

        TextField(
            modifier = modifier.fillMaxWidth(),
            value = text,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = MaterialTheme.typography.h5,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.onSecondary,
                cursorColor = MaterialTheme.colors.onBackground,
                disabledLabelColor = Color.Blue,
                focusedIndicatorColor = labelColor,
                unfocusedIndicatorColor = labelColor
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = userFieldValidatorState.textColor == ValidatorTextColors.ERROR,
            onValueChange = {
                onValueChange(it)
            },
            label = {
                val textId = userFieldValidatorState.message?.id ?: R.string.start_update_your_email
                val labelText = stringResource(id = textId)
                Text(
                    modifier = modifier.padding(
                        vertical = 8.dp
                    ),
                    text = labelText,
                    style = labelTextStyle,
                    color = labelColor,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = labelColor
                )
            },
            singleLine = true,
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(onClick = { onValueChange(String()) }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = labelColor
                        )
                    }
                }
            },
        )
    }
}