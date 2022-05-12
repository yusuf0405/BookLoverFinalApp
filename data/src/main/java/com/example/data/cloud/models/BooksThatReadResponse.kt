package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class BooksThatReadResponse(@SerializedName("results") val books: List<BookThatReadCloud>)


data class BookThatReadCloud(
    @SerializedName("progress") var progress: Int,
    @SerializedName("objectId") var objectId: String,
    @SerializedName("createdAt") var createdAt: Date,
    @SerializedName("chaptersRead") var chaptersRead: Int,
    @SerializedName("path") var path: String,
    @SerializedName("bookId") var bookId: String,
    @SerializedName("userId") var studentId: String,
    @SerializedName("isReadingPages") var isReadingPages: List<Boolean>,
)