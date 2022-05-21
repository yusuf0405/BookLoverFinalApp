package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class AddNewBookCloud(
    @SerializedName("title") var title: String,
    @SerializedName("publicYear") var publicYear: String,
    @SerializedName("schoolId") var schoolId: String,
    @SerializedName("page") var page: Int,
    @SerializedName("genres") var genres: List<String>,
    @SerializedName("author") var author: String,
    @SerializedName("poster") var poster: BookPosterCloud,
    @SerializedName("book") var book: BookPdfCloud,
    @SerializedName("chapterCount") var chapterCount: Int,
)