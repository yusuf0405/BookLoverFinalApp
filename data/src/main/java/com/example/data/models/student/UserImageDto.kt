package com.example.data.models.student

import com.google.gson.annotations.SerializedName

data class UserImageDto(
    @SerializedName("name") var name: String,
    @SerializedName("__type") var type: String,
    @SerializedName("url") var url: String,
)