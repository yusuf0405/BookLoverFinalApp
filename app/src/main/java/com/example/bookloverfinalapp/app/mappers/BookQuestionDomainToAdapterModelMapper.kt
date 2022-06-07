package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.ui.adapter.QuestionModel
import com.example.domain.Mapper
import com.example.domain.models.BookQuestionDomain

class BookQuestionDomainToAdapterModelMapper :
    Mapper<BookQuestionDomain, QuestionModel> {
    override fun map(from: BookQuestionDomain): QuestionModel = from.run {
        QuestionModel(
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