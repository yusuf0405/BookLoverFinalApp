package com.example.data.mappers

import com.example.data.models.GenreData
import com.example.domain.Mapper
import com.example.domain.models.GenreDomain
import com.example.domain.models.GenrePosterDomain
import javax.inject.Inject

class GenreDataToDomainMapper @Inject constructor() : Mapper<GenreData, GenreDomain> {
    override fun map(from: GenreData): GenreDomain = from.run {
        GenreDomain(
            id = id,
            titles = titles,
            descriptions = descriptions,
            poster = GenrePosterDomain(url = poster.url, name = poster.name)
        )
    }
}