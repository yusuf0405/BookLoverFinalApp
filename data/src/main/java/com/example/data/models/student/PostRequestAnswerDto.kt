package com.example.data.models.student

import com.example.data.data.cloud.models.UserImageCloud
import com.example.data.data.models.UserImageData
import com.google.gson.annotations.SerializedName
import java.util.*

data class PostRequestAnswerDto(
    @SerializedName("objectId") val objectId: String,
    @SerializedName("image") val image: UserImageCloud,
    @SerializedName("sessionToken") val sessionToken: String,
    @SerializedName("createdAt") val createdAt: Date,
)
