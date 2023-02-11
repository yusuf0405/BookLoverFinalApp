package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import android.widget.SearchView
import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item

data class SearchAdapterModel(
    val listener: SearchView.OnQueryTextListener,
    val queryText: String = String()
) : Item