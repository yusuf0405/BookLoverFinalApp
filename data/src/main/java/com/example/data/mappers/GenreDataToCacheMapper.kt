package com.example.data.mappers

import com.example.data.cache.models.GenreCache
import com.example.data.cache.models.GenrePosterCache
import com.example.data.models.GenreData
import com.example.data.models.GenrePosterData
import com.example.domain.Mapper
import com.example.domain.models.GenreDomain
import com.example.domain.models.GenrePosterDomain
import javax.inject.Inject

class GenreDataToCacheMapper @Inject constructor() : Mapper<GenreData, GenreCache> {

    override fun map(from: GenreData): GenreCache = from.run {
        GenreCache(
            id = id,
            titles = titles,
            descriptions = descriptions,
            poster = GenrePosterCache(url = poster.url, name = poster.name)
        )
    }
}