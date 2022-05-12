package com.example.data.cloud.mappers

import com.example.data.cloud.models.BookQuestionCloud
import com.example.data.models.BookQuestionData
import com.example.domain.Mapper

class BookQuestionCloudMapper : Mapper<BookQuestionCloud, BookQuestionData>() {
    override fun map(from: BookQuestionCloud): BookQuestionData = from.run {
        BookQuestionData(
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