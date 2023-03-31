package com.example.bookloverfinalapp.app.ui.general_screens.screen_main.mappers

import com.joseph.stories.presentation.models.StoriesAdapterItem
import com.joseph.stories.presentation.models.StoriesItemOnClickListener
import com.joseph.stories.presentation.models.UserStoriesModel
import javax.inject.Inject

interface UserStoriesToStoriesItemMapper {

    operator fun invoke(
        listener: StoriesItemOnClickListener,
        stories: UserStoriesModel
    ): StoriesAdapterItem
}

class UserStoriesToStoriesItemMapperImpl @Inject constructor() : UserStoriesToStoriesItemMapper {

    override fun invoke(
        listener: StoriesItemOnClickListener,
        stories: UserStoriesModel
    ): StoriesAdapterItem = stories.run {
        StoriesAdapterItem(
            storiesId = userId,
            userId = userId,
            previewImageUrl = previewImageUrl,
            stories = stories.stories,
            listener = listener,
            name = name
        )
    }
}