package com.example.data.models.book

import com.google.gson.annotations.SerializedName

data class BookQuestionResponse(
    @SerializedName("results") val questions: List<BookQuestionDto>,
)
