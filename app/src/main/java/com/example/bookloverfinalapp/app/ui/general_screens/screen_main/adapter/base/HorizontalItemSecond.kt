package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import com.joseph.ui.core.adapter.Item

data class HorizontalItemSecond(
    val items: List<Item>,
    var state: Parcelable? = null
) : Item