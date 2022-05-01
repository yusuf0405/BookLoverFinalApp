package com.example.data.data.cloud.mappers

import com.example.data.data.cloud.models.BookQuestionCloud
import com.example.data.data.models.BookQuestionData
import com.example.domain.domain.Mapper

class BookQuestionCloudMapper : Mapper<BookQuestionCloud, BookQuestionData>() {
    override fun map(from: BookQuestionCloud): BookQuestionData = from.run {
        BookQuestionData(
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