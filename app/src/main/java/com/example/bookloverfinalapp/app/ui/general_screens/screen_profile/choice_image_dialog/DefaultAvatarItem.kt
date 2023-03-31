package com.example.bookloverfinalapp.app.ui.general_screens.screen_profile.choice_image_dialog

import androidx.annotation.DrawableRes
import com.joseph.ui_core.adapter.Item

data class DefaultAvatarItem(
    @DrawableRes val imageResource: Int,
    val listener: DefaultAvatarItemOnClickListener
) : Item

interface DefaultAvatarItemOnClickListener {

    fun defaultAvatarOnClickListener(@DrawableRes imageResource: Int)
}