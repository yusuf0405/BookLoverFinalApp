package com.example.data.models.student

import com.google.gson.annotations.SerializedName
import java.util.*

data class PostRequestAnswerDto(
    @SerializedName("objectId") val objectId: String,
    @SerializedName("image") val image: UserImageDto,
    @SerializedName("sessionToken") val sessionToken: String,
    @SerializedName("createdAt") val createdAt: Date,
)
