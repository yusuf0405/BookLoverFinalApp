package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class BookQuestionCloud(
    @SerializedName("question") val question: String,
    @SerializedName("objectId") val id: String,
    @SerializedName("a") val a: String,
    @SerializedName("b") val b: String,
    @SerializedName("d") val d: String,
    @SerializedName("c") val c: String,
    @SerializedName("rightAnswer") val rightAnswer: String,
    @SerializedName("bookId") val bookId: String,
    @SerializedName("chapter") val chapter: String,
)