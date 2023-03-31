package com.example.data.cloud.mappers

import com.example.data.cloud.models.StoriesCloud
import com.example.data.models.StoriesData
import com.example.domain.Mapper
import javax.inject.Inject

class StoriesCloudToDataMapper @Inject constructor() : Mapper<StoriesCloud, StoriesData> {
    override fun map(from: StoriesCloud) = from.run {
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