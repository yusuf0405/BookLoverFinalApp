package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

class AudioBookCloud(
    @SerializedName("objectId") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("createdAt") val createdAt: Date,
    @SerializedName("schoolId") val schoolId: String,
    @SerializedName("isExclusive") val isExclusive: Boolean,
    @SerializedName("description") val description: String,
    @SerializedName("audio_book") val audioBookFile: AudioBookFileCloud,
    @SerializedName("genres") val genres: List<String>,
    @SerializedName("audio_book_poster") val audioBookPoster: AudioBookPosterCloud
)

data class AudioBookFileCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)

data class AudioBookPosterCloud(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)