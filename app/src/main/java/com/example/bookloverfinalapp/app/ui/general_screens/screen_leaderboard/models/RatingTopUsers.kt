package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models

import com.joseph.ui_core.adapter.Item

data class RatingTopUsers(
    var firstUserName: String,
    var firstUserLastName: String,
    var firstUserProgress: Int,
    var firstUserImageUrl: String,
    var secondUserName: String,
    var secondUserLastName: String,
    var secondUserProgress: Int,
    var secondUserImageUrl: String,
    var thirdUserName: String,
    var thirdUserLastName: String,
    var thirdUserProgress: Int,
    var thirdUserImageUrl: String,
) : Item {

    fun id() =
        firstUserName + secondUserName + thirdUserName + thirdUserImageUrl + firstUserProgress

    companion object {
        fun unknown() = RatingTopUsers(
            firstUserName = String(),
            firstUserLastName = String(),
            firstUserProgress = 0,
            firstUserImageUrl = String(),
            secondUserLastName = String(),
            secondUserName = String(),
            secondUserProgress = 0,
            thirdUserProgress = 0,
            secondUserImageUrl = String(),
            thirdUserLastName = String(),
            thirdUserName = String(),
            thirdUserImageUrl = String(),
        )
    }
}