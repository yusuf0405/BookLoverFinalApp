package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookPosterCloud
import com.example.data.cloud.models.UpdateBookCloud
import com.example.data.models.UpdateBookData
import com.example.domain.Mapper

class UpdateBookDataToCloudMapper : Mapper<UpdateBookData, UpdateBookCloud> {
    override fun map(from: UpdateBookData): UpdateBookCloud = from.run {
        UpdateBookCloud(author = author,
            publicYear = publicYear,
            title = title,
            poster = BookPosterCloud(url = poster.url, name = poster.name, type = "File"))
    }
}