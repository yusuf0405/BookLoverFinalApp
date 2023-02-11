package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*


class GenreCloud(
    @SerializedName("objectId") val id: String,
    @SerializedName("title") val titles: List<String>,
    @SerializedName("description") val descriptions: List<String>,
    @SerializedName("poster") val poster: GenrePosterCloud
)

data class GenrePosterCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)