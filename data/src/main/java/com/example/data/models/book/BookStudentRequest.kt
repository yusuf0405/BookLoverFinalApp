package com.example.data.models.book

import com.google.gson.annotations.SerializedName
import java.util.*

data class BookStudentRequest(
    @SerializedName("progress") var progress: Int,
    @SerializedName("objectId") var objectId: String,
    @SerializedName("createdAt") var createdAt: Date,
    @SerializedName("chaptersRead")  var chaptersRead: Int,
    @SerializedName("bookId") var bookId: String,
    @SerializedName("userId") var studentId: String,
    @SerializedName("isReadingPages") var isReadingPages: List<Boolean>,
)