package com.example.data.mappers

import com.example.data.models.BookPosterData
import com.example.data.models.UpdateBookData
import com.example.domain.Mapper
import com.example.domain.models.UpdateBookDomain

class UpdateBookDomainToDataMapper : Mapper<UpdateBookDomain, UpdateBookData> {
    override fun map(from: UpdateBookDomain): UpdateBookData = from.run {
        UpdateBookData(author = author,
            publicYear = publicYear,
            title = title,
            poster = BookPosterData(url = poster.url, name = poster.name))
    }
}