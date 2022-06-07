package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.ui.adapter.QuestionModel
import com.example.bookloverfinalapp.app.models.BookQuestion
import com.example.domain.Mapper

class BookQuestionAdapterModelToQuestionMapper :
    Mapper<QuestionModel, BookQuestion> {
    override fun map(from: QuestionModel): BookQuestion = from.run {
        BookQuestion(id = id,
            a = a,
            b = b,
            chapter = chapter,
            c = c,
            d = d,
            question = question,
            rightAnswer = rightAnswer,
            bookId = bookId)
    }
}