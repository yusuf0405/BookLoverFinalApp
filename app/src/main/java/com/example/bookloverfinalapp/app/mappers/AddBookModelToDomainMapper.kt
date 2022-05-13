package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.domain.Mapper
import com.example.domain.models.AddNewBookThatReadDomain
import com.example.domain.models.BookThatReadPosterDomain

class AddBookModelToDomainMapper : Mapper<AddNewBookModel, AddNewBookThatReadDomain>() {

    override fun map(from: AddNewBookModel): AddNewBookThatReadDomain = from.run {
        AddNewBookThatReadDomain(bookId = bookId, userId = userId,
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