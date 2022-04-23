package com.example.data.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class BooksThatReadResponse(@SerializedName("results") val books: List<BookThatReadCloud>)


data class BookThatReadCloud(
    @SerializedName("progress") var progress: Int,
    @SerializedName("objectId") var objectId: String,
    @SerializedName("createdAt") var createdAt: Date,
    @SerializedName("chaptersRead") var chaptersRead: Int,
    @SerializedName("bookId") var bookId: String,
    @SerializedName("userId") var studentId: String,
    @SerializedName("isReadingPages") var isReadingPages: List<Boolean>,
)