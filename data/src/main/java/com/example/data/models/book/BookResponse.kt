package com.example.data.models.book

import com.google.gson.annotations.SerializedName


data class BookResponse(
    @SerializedName("results") var books: List<BookDto>
)