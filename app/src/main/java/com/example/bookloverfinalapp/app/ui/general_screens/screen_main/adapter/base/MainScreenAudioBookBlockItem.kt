package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import android.os.Parcelable

data class MainScreenAudioBookBlockItem(
    val items: List<Item>,
    var state: Parcelable? = null
) : Item