package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName

data class AddStoriesCloud(
    @SerializedName("userId") val userId: String,
    @SerializedName("schoolId") val schoolId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("imageFileUrl") val imageFileUrl: String,
    @SerializedName("previewImageUrl") var previewImageUrl: String,
    @SerializedName("videoFileUrl") var videoFileUrl: String,
    @SerializedName("isVideoFile") val isVideoFile: Boolean,
)