package com.example.data.mappers

import com.example.data.cache.models.StoriesCache
import com.example.data.models.StoriesData
import com.example.domain.Mapper
import javax.inject.Inject

class StoriesDataToCacheMapper @Inject constructor() : Mapper<StoriesData, StoriesCache> {
    override fun map(from: StoriesData) = from.run {
        StoriesCache(
            storiesId = storiesId,
            userId = userId,
            schoolId = schoolId,
            title = title,
            description = description,
            imageFileUrl = imageFileUrl,
            previewImageUrl = previewImageUrl,
            videoFileUrl = videoFileUrl,
            isVideoFile = isVideoFile,
            publishedDate = publishedDate
        )
    }
}