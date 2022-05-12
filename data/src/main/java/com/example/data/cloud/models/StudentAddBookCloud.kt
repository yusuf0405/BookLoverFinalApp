package com.example.data.cloud.models

import com.example.domain.models.BookThatReadDomain
import com.google.gson.annotations.SerializedName

data class StudentAddBookCloud(
    @SerializedName("books") var books: List<BookThatReadDomain>,
)