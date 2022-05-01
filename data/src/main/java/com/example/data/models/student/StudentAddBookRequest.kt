package com.example.data.models.student

import com.example.domain.domain.models.BookThatReadDomain
import com.google.gson.annotations.SerializedName

data class StudentAddBookRequest(
    @SerializedName("books") var books: List<BookThatReadDomain>,
)