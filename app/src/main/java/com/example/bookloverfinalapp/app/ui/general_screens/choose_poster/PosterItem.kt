package com.example.bookloverfinalapp.app.ui.general_screens.choose_poster

import com.joseph.ui_core.adapter.Item


data class PosterItem(
    val type: PhotoType = PhotoType.NETWORK,
    val posterUrl: String = String(),
    val posterFileUri: String = String(),
    val onClickListener: (String) -> Unit
) : Item

enum class PhotoType {
    NETWORK,
    LOCAL,
}