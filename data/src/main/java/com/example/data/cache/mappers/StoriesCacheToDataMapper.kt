package com.example.data.cache.mappers

import com.example.data.cache.models.StoriesCache
import com.example.data.models.StoriesData
import com.example.domain.Mapper
import javax.inject.Inject

class StoriesCacheToDataMapper @Inject constructor(): Mapper<StoriesCache, StoriesData> {
    override fun map(from: StoriesCache) = from.run {
        StoriesData(
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