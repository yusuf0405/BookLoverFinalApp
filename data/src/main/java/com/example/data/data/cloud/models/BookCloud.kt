package com.example.data.data.cloud.models

import com.google.gson.annotations.SerializedName

data class BookCloud(
    @SerializedName("author") var author: String,
    @SerializedName("createdAt") var createdAt: String,
    @SerializedName("objectId") var id: String,
    @SerializedName("page") var page: Int,
    @SerializedName("publicYear") var publicYear: String,
    @SerializedName("book") var book: BookPdfCloud,
    @SerializedName("title") var title: String,
    @SerializedName("chapterCount") var chapterCount: Int,
    @SerializedName("poster") var poster: BookPosterCloud,
    @SerializedName("updatedAt") var updatedAt: String,
)

data class BookPdfCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)

data class BookPosterCloud(
    @SerializedName("name") var name: String,
    @SerializedName("url") var url: String,
)
