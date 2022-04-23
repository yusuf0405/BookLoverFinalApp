package com.example.data.models.book

import com.google.gson.annotations.SerializedName


data class BookPdfDto(
    @SerializedName("name") var name: String,
    @SerializedName("type") var type: String,
    @SerializedName("url") var url: String,
)