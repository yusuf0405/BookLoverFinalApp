package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.AddNewBook
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookDomain
import com.example.domain.models.BookPdfDomain
import com.example.domain.models.BookPosterDomain

class AddNewBookToDomainMapper : Mapper<AddNewBook, AddNewBookDomain> {
    override fun map(from: AddNewBook): AddNewBookDomain = from.run {
        AddNewBookDomain(
            title = title,
            publicYear = publicYear,
            page = page,
            author = author,
            poster = BookPosterDomain(name = poster.name, url = poster.url),
            book = BookPdfDomain(name = book.name, type = "File", url = book.url),
            chapterCount = chapterCount, schoolId = schoolId,
            genres = genres,
            description = description,
            isExclusive = isExclusive
        )
    }
}