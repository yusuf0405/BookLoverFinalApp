package com.example.data.mappers

import com.example.data.cloud.models.AddStoriesCloud
import com.example.data.models.AddStoriesData
import com.example.domain.Mapper
import com.example.domain.models.AddStoriesDomain
import javax.inject.Inject

class AddStoriesDataToCloudMapper @Inject constructor() :
    Mapper<AddStoriesData, AddStoriesCloud> {

    override fun map(from: AddStoriesData) = from.run {
        AddStoriesCloud(
            userId = userId,
            schoolId = schoolId,
            title = title,
            description = description,
            imageFileUrl = imageFileUrl,
            previewImageUrl = previewImageUrl,
            videoFileUrl = videoFileUrl,
            isVideoFile = isVideoFile
        )
    }
}