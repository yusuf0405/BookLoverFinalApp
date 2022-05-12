package com.example.bookloverfinalapp.app.mappers

import com.example.bookloverfinalapp.app.models.AddBookQuestion
import com.example.domain.Mapper
import com.example.domain.models.AddBookQuestionDomain

class AddBookQuestionToDomainMapper : Mapper<AddBookQuestion, AddBookQuestionDomain>() {
    override fun map(from: AddBookQuestion): AddBookQuestionDomain = from.run {
        AddBookQuestionDomain(a = a,
            b = b,
            chapter = chapter,
            c = c,
            d = d,
            question = question,
            rightAnswer = rightAnswer,
            bookId = bookId)
    }
}