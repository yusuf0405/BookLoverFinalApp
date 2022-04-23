package com.example.data.models.student

import com.google.gson.annotations.SerializedName

data class UpdateDto(
    @SerializedName("updatedAt") val updatedAt: String,
)