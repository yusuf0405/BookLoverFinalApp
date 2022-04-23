package com.example.data.models.book

import com.google.gson.annotations.SerializedName

data class BookPosterDto(
    @SerializedName("name")  var name: String,
    @SerializedName("url") var url: String,
)