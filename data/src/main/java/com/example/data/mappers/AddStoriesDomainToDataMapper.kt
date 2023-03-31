package com.example.data.mappers

import com.example.data.models.AddStoriesData
import com.example.domain.Mapper
import com.example.domain.models.AddStoriesDomain
import javax.inject.Inject

class AddStoriesDomainToDataMapper @Inject constructor() :
    Mapper<AddStoriesDomain, AddStoriesData> {

    override fun map(from: AddStoriesDomain) = from.run {
        AddStoriesData(
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