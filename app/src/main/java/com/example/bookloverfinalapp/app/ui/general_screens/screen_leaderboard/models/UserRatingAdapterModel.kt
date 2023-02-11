package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models

import com.example.bookloverfinalapp.app.ui.general_screens.screen_main.adapter.base.Item


data class UserRatingAdapterModel(
    val name: String,
    val lastName: String,
    val progress: Int,
    val userRatingPosition: Int,
    val className: String,
    val imageUrl:String
) : Item {

    fun id() = name + lastName + className
}