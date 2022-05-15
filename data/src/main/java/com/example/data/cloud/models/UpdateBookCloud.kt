package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class UpdateBookCloud(
    @SerializedName("author") var author: String,
    @SerializedName("publicYear") var publicYear: String,
    @SerializedName("title") var title: String,
    @SerializedName("poster") var poster: BookPosterCloud,
)
