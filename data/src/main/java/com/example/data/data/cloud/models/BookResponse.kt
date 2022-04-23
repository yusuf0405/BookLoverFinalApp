package com.example.data.data.cloud.models

import com.example.data.data.cloud.models.BookCloud
import com.google.gson.annotations.SerializedName


data class BookResponse(
    @SerializedName("results")
    var books: List<BookCloud>,
)