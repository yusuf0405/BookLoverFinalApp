package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class AddNewBookCloud(
    @SerializedName("title") var title: String,
    @SerializedName("publicYear") var publicYear: String,
    @SerializedName("page") var page: Int,
    @SerializedName("author") var author: String,
    @SerializedName("poster") var poster: BookPosterCloud,
    @SerializedName("book") var book: BookPdfCloud,
    @SerializedName("chapterCount") var chapterCount: Int,
)