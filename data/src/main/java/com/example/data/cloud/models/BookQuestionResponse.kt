package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class BookQuestionResponse(
    @SerializedName("results") val questions: List<BookQuestionCloud>,
)
