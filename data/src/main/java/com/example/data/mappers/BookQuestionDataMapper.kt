package com.example.data.mappers

import com.example.data.models.BookQuestionData
import com.example.domain.Mapper
import com.example.domain.models.BookQuestionDomain

class BookQuestionDataMapper : Mapper<BookQuestionData, BookQuestionDomain>() {
    override fun map(from: BookQuestionData): BookQuestionDomain = from.run {
        BookQuestionDomain(
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