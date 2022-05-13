package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class AddNewBookThatReadCloud(
    @SerializedName("progress") val progress: Int,
    @SerializedName("bookId") val bookId: String,
    @SerializedName("userId") val userId: String,
    @SerializedName("isReadingPages") val isReadingPages: List<Boolean>,
    @SerializedName("chaptersRead") val chaptersRead: Int,
    @SerializedName("path") val path: String,
)