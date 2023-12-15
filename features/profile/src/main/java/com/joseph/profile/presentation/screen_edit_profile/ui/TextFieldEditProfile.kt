package com.joseph.profile.presentation.screen_edit_profile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseph.profile.domain.models.UserFieldValidatorState
import com.joseph.profile.domain.models.ValidatorTextColors
import com.joseph.profile.domain.models.ValidatorTextStyle
import com.joseph.ui.core.R

@Composable
internal fun TextFieldProfile(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter,
    userFieldValidatorState: UserFieldValidatorState,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    val labelColor = when (userFieldValidatorState.textColor) {
        ValidatorTextColors.DEFAULT -> MaterialTheme.colors.secondary
        ValidatorTextColors.ERROR -> MaterialTheme.colors.error
        ValidatorTextColors.SUCCESS -> MaterialTheme.colors.primary
    }

    val textId = userFieldValidatorState.message?.id ?: R.string.start_update_your_email
    val labelText = stringResource(id = textId)
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 26.dp),
    ) {

        AnimatedVisibility(
            visible = labelText == stringResource(id = R.string.everything_is_correct),
        ) {
            Text(
                modifier = modifier.padding(
                    bottom = 6.dp,
                    start = 4.dp
                ),
                text = labelText,
                color = labelColor,
                fontSize = 12.sp
            )
        }
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 2.dp,
                    shape = RoundedCornerShape(10.dp)
                ),
            value = text,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                textColor = labelColor,
                backgroundColor = MaterialTheme.colors.onSecondary,
                cursorColor = labelColor,
                focusedIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Transparent,
                unfocusedLabelColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            leadingIcon = {
                Row(
                    modifier = modifier
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = modifier.size(20.dp),
                        painter = painter,
                        contentDescription = null,
                        tint = labelColor,
                    )
                    Spacer(modifier = modifier.width(8.dp))
                    Spacer(
                        modifier = modifier
                            .width(1.dp)
                            .height(24.dp)
                            .background(labelColor)
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            textStyle = MaterialTheme.typography.h5,
            trailingIcon = {
                if (text.isNotEmpty() && !isPassword) {
                    IconButton(onClick = { onValueChange(String()) }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null,
                            tint = labelColor
                        )
                    }
                } else {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }) {
                        Icon(
                            imageVector = if (isPasswordVisible)
                                Icons.Filled.Delete
                            else
                                Icons.Filled.Done,
                            contentDescription = null
                        )
                    }
                }
            },
        )
        AnimatedVisibility(
            visible = labelText.isNotEmpty()
                    && labelText != stringResource(id = R.string.everything_is_correct),
            enter = fadeIn(tween(350)) + expandVertically(
                animationSpec = tween(
                    durationMillis = 350,
                )
            ),
            exit = shrinkVertically(
                animationSpec = tween(
                    durationMillis = 350,
                )
            ) + fadeOut(tween(350))
        ) {
            Text(
                modifier = modifier.padding(
                    top = 4.dp,
                    start = 4.dp
                ),
                text = labelText,
                color = labelColor,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
internal fun TextFieldEditProfile(
    modifier: Modifier = Modifier,
    text: String,
    painter: Painter,
    userFieldValidatorState: UserFieldValidatorState,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val labelColor = when (userFieldValidatorState.textColor) {
        ValidatorTextColors.DEFAULT -> MaterialTheme.colors.secondary
        ValidatorTextColors.ERROR -> MaterialTheme.colors.error
        ValidatorTextColors.SUCCESS -> MaterialTheme.colors.primary
    }
    val labelTextStyle = when (userFieldValidatorState.texStyle) {
        ValidatorTextStyle.DEFAULT -> MaterialTheme.typography.caption.copy(fontSize = 14.sp)
        ValidatorTextStyle.LITTLE -> MaterialTheme.typography.caption.copy(fontSize = 11.sp)
    }

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        value = text,
        visualTransformation = if (isPasswordVisible) PasswordVisualTransformation()
        else VisualTransformation.None,
        textStyle = MaterialTheme.typography.h5,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            cursorColor = MaterialTheme.colors.onBackground,
            disabledLabelColor = Color.Blue,
            focusedIndicatorColor = labelColor,
            unfocusedIndicatorColor = labelColor,
        ),
        shape = RoundedCornerShape(12.dp),
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
                painter = painter,
                contentDescription = null,
                tint = labelColor
            )
        },
        singleLine = true,
        trailingIcon = {
            if (text.isNotEmpty() && !isPassword) {
                IconButton(onClick = { onValueChange(String()) }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                        tint = labelColor
                    )
                }
            }
            if (isPassword) {
                IconButton(
                    onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                    Icon(
                        imageVector = if (isPasswordVisible)
                            Icons.Filled.Delete
                        else
                            Icons.Filled.Done,
                        contentDescription = null
                    )
                }
            }
        },
    )
}



@Composable
fun PhoneField(
    phone: String,
    modifier: Modifier = Modifier,
    mask: String = "000 000 000",
    maskNumber: Char = '5',
    onPhoneChanged: (String) -> Unit = {}
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        value = phone,
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        textStyle = MaterialTheme.typography.h5,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.background,
            cursorColor = MaterialTheme.colors.onBackground,
            disabledLabelColor = Color.Blue,
            focusedIndicatorColor = MaterialTheme.colors.onBackground,
            unfocusedIndicatorColor = MaterialTheme.colors.onBackground,
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp),
        onValueChange = {
            onPhoneChanged(it.take(mask.count { it == maskNumber }))
        },
        leadingIcon = {
            Row(
                modifier = modifier
                    .padding(start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = modifier.padding(start = 4.dp),
                    painter = painterResource(id = R.drawable.about_app_icon),
                    contentDescription = null,
                )
                IconButton(
                    onClick = {
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                    )
                }
                Text(
                    text = "+996",
                    style = MaterialTheme.typography.h5,
                )
            }

        },
        placeholder = {
            Text(
                text = mask,
                style = MaterialTheme.typography.h5.copy(color = Color.Gray),

                )
        },
        singleLine = true,
        trailingIcon = {
            if (phone.isNotEmpty()) {
                IconButton(onClick = { onPhoneChanged(String()) }) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = null,
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextFieldWithHint(
    phone: String,
    modifier: Modifier = Modifier,
    mask: String = "000 0",
    maskNumber: Char = '-',
    onPhoneChanged: (String) -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }
    val colors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.primary,
        disabledTextColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.background,
        cursorColor = MaterialTheme.colors.onBackground,
        disabledLabelColor = Color.Blue,
    )
    val textStyle = TextStyle.Default

    BasicTextField(
        modifier = modifier
            .padding(top = 32.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 0.dp,
                shape = RoundedCornerShape(12.dp)
            ),
        value = phone,
        onValueChange = { ss ->
            onPhoneChanged(ss.take(mask.count { it == maskNumber }))
        },

        interactionSource = interactionSource,
        textStyle = MaterialTheme.typography.h5,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        visualTransformation = PhoneVisualTransformation(mask, maskNumber),
        decorationBox = { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = phone,
                innerTextField = {
                    Box {
                        if (phone.isEmpty()) {
                            Text(
                                modifier = modifier.fillMaxSize(),
                                text = mask,
                                style = textStyle.copy(color = Color.Gray),
                            )
                        }

                        innerTextField()
                    }
                },

                interactionSource = interactionSource,
                colors = colors,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
            )
        }
    )
}

class PhoneVisualTransformation(
    val mask: String,
    val maskNumber: Char
) : VisualTransformation {

    private val maxLength = mask.count { it == maskNumber }

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.length > maxLength) text.take(maxLength) else text

        val annotatedString = buildAnnotatedString {
            if (trimmed.isEmpty()) return@buildAnnotatedString
            var maskIndex = 0
            var textIndex = 0
            while (textIndex < trimmed.length && maskIndex < mask.length) {
                if (mask[maskIndex] != maskNumber) {
                    val nextDigitIndex = mask.indexOf(maskNumber, maskIndex)
                    append(mask.substring(maskIndex, nextDigitIndex))
                    maskIndex = nextDigitIndex
                }
                append(trimmed[textIndex++])
                maskIndex++
            }
        }

        return TransformedText(annotatedString, PhoneOffsetMapper(mask, maskNumber))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
//        if (other !is PhonedVisualTransformation) return false
//        if (mask != other.mask) return false
//        if (maskNumber != other.maskNumber) return false
        return true
    }

    override fun hashCode(): Int {
        return mask.hashCode()
    }
}

private class PhoneOffsetMapper(
    val mask: String,
    val numberChar: Char
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        return try {
            var noneDigitCount = 0
            var i = 0
            while (i < offset + noneDigitCount) {
                if (mask[i++] != numberChar) noneDigitCount++
            }
            offset + noneDigitCount
        } catch (e: Exception) {
            0
        }

    }

    override fun transformedToOriginal(offset: Int): Int =
        offset - mask.take(offset).count { it != numberChar }
}