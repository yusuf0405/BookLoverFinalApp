package com.example.data.cloud.mappers

import com.example.data.cloud.models.GenreCloud
import com.example.data.models.GenreData
import com.example.data.models.GenrePosterData
import com.example.domain.Mapper
import javax.inject.Inject

class GenreCloudToDataMapper @Inject constructor() : Mapper<GenreCloud, GenreData> {
    override fun map(from: GenreCloud): GenreData = from.run {
        GenreData(
            id = id,
            titles = titles,
            descriptions = descriptions,
            poster = GenrePosterData(url = poster.url, name = poster.name)
        )
    }
}