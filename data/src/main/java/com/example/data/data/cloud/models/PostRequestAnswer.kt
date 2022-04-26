package com.example.data.data.cloud.models

import com.google.gson.annotations.SerializedName
import java.util.*

data class PostRequestAnswer(
    @SerializedName("objectId") val objectId: String,
    @SerializedName("createdAt") val createdAt: Date,
)
