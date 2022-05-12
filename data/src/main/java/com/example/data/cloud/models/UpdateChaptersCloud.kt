package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

class UpdateChaptersCloud(
    @SerializedName("isReadingPages") val isReadingPages: List<Boolean>,
    @SerializedName("chaptersRead") val chaptersRead: Int,
)