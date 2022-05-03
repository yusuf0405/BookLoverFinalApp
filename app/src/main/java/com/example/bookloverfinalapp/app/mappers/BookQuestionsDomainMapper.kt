package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookQuestionDomain

class BookQuestionsDomainMapper : Mapper<BookQuestionDomain, BookQuestion>() {
    override fun map(from: BookQuestionDomain): BookQuestion = from.run {
        BookQuestion(
            a = a,
            b = b,
            chapter = chapter,
            c = c,
            d = d,
            id = id,
            question = question,
            rightAnswer = rightAnswer,
            bookId = bookId
        )
    }
}