package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base

import com.example.bookloverfinalapp.app.models.Collections
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.listeners.CollectionItemOnClickListener
import com.joseph.ui.core.adapter.Item

data class CollectionItem(
    val collections: Collections,
    val listener: CollectionItemOnClickListener
) : Item