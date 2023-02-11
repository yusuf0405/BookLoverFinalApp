package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.Genre
import com.example.bookloverfinalapp.app.models.GenrePoster
import com.example.data.models.GenreData
import com.example.domain.Mapper
import com.example.domain.models.GenreDomain
import com.example.domain.models.GenrePosterDomain
import javax.inject.Inject

class GenreDomainToUiMapper @Inject constructor() : Mapper<GenreDomain, Genre> {
    override fun map(from: GenreDomain): Genre = from.run {
        Genre(
            id = id,
            titles = titles,
            descriptions = descriptions,
            poster = GenrePoster(url = poster.url, name = poster.name)
        )
    }
}