package com.joseph.stories.presentation.mappers

import com.joseph.stories.presentation.models.StoriesModel
import com.joseph.stories.presentation.models.UserStoriesModel
import javax.inject.Inject

interface StoriesToUserStoriesMapper {

    operator fun invoke(stories: List<StoriesModel>): List<UserStoriesModel>
}

class StoriesToUserStoriesMapperImpl @Inject constructor() : StoriesToUserStoriesMapper {

    override fun invoke(stories: List<StoriesModel>): List<UserStoriesModel> {
        val userStories = mutableListOf<UserStoriesModel>()
        val userIds = stories.map { it.userId }.toSet()
        val userPreviewImageUrls = stories.map { it.previewImageUrl }.toSet().toList()
        userIds.forEachIndexed { index, userId ->
            userStories.add(
                UserStoriesModel(
                    userId = userId,
                    previewImageUrl = userPreviewImageUrls[index],
                    stories = stories.filter { it.userId == userId },
                    name = stories[index].title
                )
            )
        }
        return userStories
    }
}