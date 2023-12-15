package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.models

import android.widget.SearchView
import com.joseph.ui.core.adapter.Item

data class SearchAdapterModel(
    val listener: SearchView.OnQueryTextListener,
    val queryText: String = String()
) : Item