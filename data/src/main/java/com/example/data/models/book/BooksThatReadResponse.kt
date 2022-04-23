package com.example.data.models.book

import com.google.gson.annotations.SerializedName

data class BooksThatReadResponse(
    @SerializedName("results") val books: List<BooksThatReadDto>,
)
