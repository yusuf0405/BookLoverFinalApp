package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item
import com.example.data.cache.models.IdResourceString

data class HeaderItem(
    val onClickListener: () -> Unit,
    val titleId: IdResourceString,
    val showMoreIsVisible: Boolean = true
) : Item