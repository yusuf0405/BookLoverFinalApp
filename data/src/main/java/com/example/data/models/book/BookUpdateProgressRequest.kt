package com.example.data.models.book

import com.google.gson.annotations.SerializedName

data class BookUpdateProgressRequest(
    @SerializedName("progress") val progress: Int,
)