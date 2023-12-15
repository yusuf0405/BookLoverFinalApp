package com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.models

import com.example.bookloverfinalapp.app.ui.general_screens.screen_all_students.listener.UserItemOnClickListener
import com.joseph.ui.core.adapter.Item

data class UserAdapterModel(
    val id: String,
    val name: String,
    val lastName: String,
    val imageUrl: String,
    val booksReadCount: Int = 0,
    val chaptersReadCount: Int = 0,
    val pagesReadCount: Int = 0,
    val listener: UserItemOnClickListener
) : Item {

    fun fullName() = "$lastName $name"
}