package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable
import com.joseph.ui_core.adapter.Item

data class BookHorizontalItem(
    val items: List<Item>,
    var state: Parcelable? = null
) : Item