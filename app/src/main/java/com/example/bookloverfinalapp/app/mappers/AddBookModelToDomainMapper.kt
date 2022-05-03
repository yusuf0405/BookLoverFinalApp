package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.BookThatReadPosterDomain

class AddBookModelToDomainMapper : Mapper<AddNewBookModel, AddNewBookDomain>() {

    override fun map(from: AddNewBookModel): AddNewBookDomain = from.run {
        AddNewBookDomain(bookId = bookId, userId = userId,
            chaptersRead = chaptersRead,
            author = author,
            progress = progress,
            page = page,
            publicYear = publicYear,
            title = title,
            chapterCount = chapterCount,
            poster = BookThatReadPosterDomain(name = poster.name, url = poster.url),
            book = book,
            isReadingPages = isReadingPages
        )
    }
}