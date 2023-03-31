package com.example.data.mappers

import com.example.data.models.StoriesData
import com.example.domain.Mapper
import com.example.domain.models.StoriesDomain
import javax.inject.Inject

class StoriesDataToDomainMapper @Inject constructor(): Mapper<StoriesData, StoriesDomain> {
    override fun map(from: StoriesData) = from.run {
        StoriesDomain(
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