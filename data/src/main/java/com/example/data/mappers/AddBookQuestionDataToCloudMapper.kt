package com.example.data.mappers

import com.example.data.cloud.models.AddBookQuestionCloud
import com.example.data.models.AddBookQuestionData
import com.example.domain.Mapper


class AddBookQuestionDataToCloudMapper : Mapper<AddBookQuestionData, AddBookQuestionCloud> {
    override fun map(from: AddBookQuestionData): AddBookQuestionCloud = from.run {
        AddBookQuestionCloud(
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