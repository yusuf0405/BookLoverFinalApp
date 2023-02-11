package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter

import android.os.Parcelable
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item

data class PopularGenreBlockItem(
    val items: List<Item>,
    var state: Parcelable? = null
) : Item