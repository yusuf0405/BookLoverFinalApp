package com.example.data.mappers

import com.example.data.models.AddNewBookData
import com.example.data.models.BookPdfData
import com.example.data.models.BookPosterData
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookDomain

class AddBookDomainToDataMapper : Mapper<AddNewBookDomain, AddNewBookData>() {
    override fun map(from: AddNewBookDomain): AddNewBookData = from.run {
        AddNewBookData(
            title = title,
            publicYear = publicYear,
            page = page,
            author = author,
            poster = BookPosterData(name = poster.name, url = poster.url),
            book = BookPdfData(name = book.name, type = book.type, url = book.url),
            chapterCount = chapterCount)
    }
}