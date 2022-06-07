package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.domain.Mapper
import com.example.domain.models.BookQuestionDomain

class BookQuestionsDomainMapper : Mapper<BookQuestionDomain, BookQuestion> {
    override fun map(from: BookQuestionDomain): BookQuestion = from.run {
        BookQuestion(
            id = id,
            a = a,
            b = b,
            chapter = chapter,
            c = c,
            d = d,
            question = question,
            rightAnswer = rightAnswer,
            bookId = bookId
        )
    }
}