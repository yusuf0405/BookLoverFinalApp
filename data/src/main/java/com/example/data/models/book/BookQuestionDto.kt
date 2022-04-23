package com.example.data.models.book

import com.google.gson.annotations.SerializedName

class BookQuestionDto(
    @SerializedName("objectId") val id: String,
    @SerializedName("question") val question: String,
    @SerializedName("a") val a: String,
    @SerializedName("b") val b: String,
    @SerializedName("d") val d: String,
    @SerializedName("c") val c: String,
    @SerializedName("rightAnswer") val rightAnswer: String,
    @SerializedName("bookId") val bookId: String,
    @SerializedName("chapter") val chapter: Int,
)