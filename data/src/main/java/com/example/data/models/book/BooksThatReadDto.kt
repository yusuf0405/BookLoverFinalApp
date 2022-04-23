package com.example.data.models.book


import com.google.gson.annotations.SerializedName

data class BooksThatReadDto(
    @SerializedName("bookId") var bookId: String,
    @SerializedName("chaptersRead") var chaptersRead: Int,
    @SerializedName("isReadingPages") var isReadingPages: List<Boolean>,
    @SerializedName("progress") var progress: Int,
    @SerializedName("userId") var userId: String,
)