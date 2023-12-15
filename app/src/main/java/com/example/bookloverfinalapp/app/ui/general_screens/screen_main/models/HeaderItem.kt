package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.joseph.ui.core.adapter.Item
import com.example.data.cache.models.IdResourceString

data class HeaderItem(
    val titleId: IdResourceString,
    val showMoreIsVisible: Boolean = true,
    val onClickListener: () -> Unit = {},
) : Item