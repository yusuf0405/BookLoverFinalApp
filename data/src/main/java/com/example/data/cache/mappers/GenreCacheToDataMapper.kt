package com.example.data.cache.mappers

import com.example.data.cache.models.GenreCache
import com.example.data.cloud.models.GenreCloud
import com.example.data.models.GenreData
import com.example.data.models.GenrePosterData
import com.example.domain.Mapper
import javax.inject.Inject

class GenreCacheToDataMapper @Inject constructor() : Mapper<GenreCache, GenreData> {
    override fun map(from: GenreCache): GenreData = from.run {
        GenreData(
            id = id,
            titles = titles,
            descriptions = descriptions,
            poster = GenrePosterData(url = poster.url, name = poster.name)
        )
    }
}