package com.example.bookloverfinalapp.app.mappers

import com.example.domain.Mapper
import com.example.domain.models.StoriesDomain
import com.joseph.stories.presentation.models.MediaType
import com.joseph.stories.presentation.models.StoriesModel
import javax.inject.Inject

class StoriesDomainToUiMapper @Inject constructor() : Mapper<StoriesDomain,StoriesModel> {
    override fun map(from: StoriesDomain) = from.run {
        StoriesModel(
            storiesId = storiesId,
            userId = userId,
            schoolId = schoolId,
            title = title,
            description = description,
            imageFileUrl = imageFileUrl,
            previewImageUrl = previewImageUrl,
            videoFileUrl = videoFileUrl,
            mediaType = if (isVideoFile) MediaType.VIDEO else MediaType.IMAGE,
            publishedDate = publishedDate
        )
    }
}