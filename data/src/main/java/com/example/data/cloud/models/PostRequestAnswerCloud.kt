package com.example.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class PostRequestAnswerCloud(
    @SerializedName("objectId") val objectId: String,
    @SerializedName("createdAt") val createdAt: Date,
)
