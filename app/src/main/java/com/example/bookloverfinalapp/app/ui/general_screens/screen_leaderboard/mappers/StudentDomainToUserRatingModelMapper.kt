package com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.mappers

import com.joseph.ui.core.adapter.Item
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models.RatingTopUsers
import com.example.bookloverfinalapp.app.ui.general_screens.screen_leaderboard.models.UserRatingAdapterModel
import com.example.domain.models.StudentDomain
import javax.inject.Inject

interface StudentDomainToUserRatingModelMapper {

    fun map(users: List<StudentDomain>): List<Item>
}

class StudentDomainToUserRatingModelMapperImpl @Inject constructor() :
    StudentDomainToUserRatingModelMapper {

    override fun map(users: List<StudentDomain>): List<Item> {
        val ratingTopUsers = RatingTopUsers.unknown()
        val sortedUsers = users.sortedByDescending { it.progress }
        val userRatingList = mutableListOf<UserRatingAdapterModel>()
        sortedUsers.forEachIndexed { index, student ->
            when (index) {
                0 -> {
                    ratingTopUsers.firstUserName = student.name
                    ratingTopUsers.firstUserProgress = student.progress
                    ratingTopUsers.firstUserLastName = student.lastname
                    ratingTopUsers.firstUserImageUrl = student.image.url
                }
                1 -> {
                    ratingTopUsers.secondUserName = student.name
                    ratingTopUsers.secondUserProgress = student.progress
                    ratingTopUsers.secondUserLastName = student.lastname
                    ratingTopUsers.secondUserImageUrl = student.image.url
                }
                2 -> {
                    ratingTopUsers.thirdUserName = student.name
                    ratingTopUsers.thirdUserProgress = student.progress
                    ratingTopUsers.thirdUserLastName = student.lastname
                    ratingTopUsers.thirdUserImageUrl = student.image.url
                }
                else -> {
                    val ratingModel = UserRatingAdapterModel(
                        name = student.name,
                        lastName = student.lastname,
                        progress = student.progress,
                        imageUrl = student.image.url,
                        userRatingPosition = (index + 1),
                        className = student.className
                    )
                    userRatingList.add(ratingModel)
                }
            }
        }
        val allItems = mutableListOf<Item>()
        if (ratingTopUsers != RatingTopUsers.unknown()) allItems.add(ratingTopUsers)
        allItems.addAll(userRatingList)
        return allItems
    }
}