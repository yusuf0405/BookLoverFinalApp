package com.example.data.data.mappers

import com.example.data.data.models.BookQuestionData
import com.example.domain.domain.Mapper
import com.example.domain.domain.models.BookQuestionDomain

class BookQuestionDataMapper : Mapper<BookQuestionData, BookQuestionDomain>() {
    override fun map(from: BookQuestionData): BookQuestionDomain = from.run {
        BookQuestionDomain(
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