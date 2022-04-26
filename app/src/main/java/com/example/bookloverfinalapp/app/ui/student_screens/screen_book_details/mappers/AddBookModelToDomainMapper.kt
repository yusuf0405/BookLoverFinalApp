package com.example.bookloverfinalapp.app.ui.student_screens.screen_book_details.mappers

import com.example.bookloverfinalapp.app.models.AddNewBookModel
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.AddNewBookDomain
import com.example.domain.domain.models.StudentBookPdfDomain
import com.example.domain.domain.models.StudentBookPosterDomain

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
            poster = StudentBookPosterDomain(name = poster.name, url = poster.url),
            book = StudentBookPdfDomain(name = book.name, url = book.url),
            isReadingPages = isReadingPages
        )
    }
}